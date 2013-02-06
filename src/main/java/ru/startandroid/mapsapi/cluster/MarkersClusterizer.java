package ru.startandroid.mapsapi.cluster;


import android.graphics.Point;
import android.os.AsyncTask;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Utility class for cluster markers
 */
public class MarkersClusterizer {
    private static GoogleMap map;
    private static int interval;

    /**
     * This method will clusterize markers and draw it on the given map instance
     * @param googleMap target {@link com.google.android.gms.maps.GoogleMap} instance
     * @param markers list of all {@link com.google.android.gms.maps.model.MarkerOptions}
     * @param i interval between two markers
     */
    public static void clusterMarkers(GoogleMap googleMap, ArrayList<MarkerOptions> markers, int i) {
        map = googleMap;
        interval = i;
        Projection projection = map.getProjection();
        LinkedHashMap<MarkerOptions, Point> points = new LinkedHashMap<MarkerOptions, Point>();
        for (MarkerOptions markerOptions : markers) {
            points.put(markerOptions, projection.toScreenLocation(markerOptions.getPosition()));
            markerOptions.title("");
        }
        map.clear();

        CheckMarkersTask checkMarkersTask = new CheckMarkersTask();
        checkMarkersTask.execute(points);
    }

    private static class CheckMarkersTask extends AsyncTask<LinkedHashMap<MarkerOptions, Point>, Void, LinkedHashMap<Point, ArrayList<MarkerOptions>>> {

        private double findDistance(float x1, float y1, float x2, float y2) {
            return Math.sqrt(((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1)));
        }

        @Override
        protected LinkedHashMap<Point, ArrayList<MarkerOptions>> doInBackground(LinkedHashMap<MarkerOptions, Point>... params) {
            LinkedHashMap<Point, ArrayList<MarkerOptions>> clusters = new LinkedHashMap<Point, ArrayList<MarkerOptions>>();
            LinkedHashMap<MarkerOptions, Point> points = params[0];
            for (MarkerOptions markerOptions : points.keySet()) {
                Point point = points.get(markerOptions);
                double minDistance = -1;
                Point nearestPoint = null;
                double currentDistance;
                for (Point existingPoint : clusters.keySet()) {
                    currentDistance = findDistance(point.x, point.y, existingPoint.x, existingPoint.y);
                    if (currentDistance <= interval) {
                        if ((currentDistance < minDistance) || (minDistance == -1)) {
                            minDistance = currentDistance;
                            nearestPoint = existingPoint;
                        }
                    }
                }

                if (nearestPoint != null) {
                    clusters.get(nearestPoint).add(markerOptions);
                } else {
                    ArrayList<MarkerOptions> markersForPoint = new ArrayList<MarkerOptions>();
                    markersForPoint.add(markerOptions);
                    clusters.put(point, markersForPoint);
                }
            }
            return clusters;
        }

        @Override
        protected void onPostExecute(LinkedHashMap<Point, ArrayList<MarkerOptions>> clusters) {
            for (Point point : clusters.keySet()) {
                ArrayList<MarkerOptions> markersForPoint = clusters.get(point);
                MarkerOptions mainMarker = markersForPoint.get(0);
                if (markersForPoint.size() > 1) {
                    mainMarker.title(Integer.toString(markersForPoint.size()));
                }
                map.addMarker(mainMarker);
            }
        }
    }
}
