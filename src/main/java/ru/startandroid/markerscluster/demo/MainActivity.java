
package ru.startandroid.markerscluster.demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import ru.startandroid.markerscluster.R;
import ru.startandroid.markerscluster.cluster.MarkersClusterizer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;

/**
 * Demo activity. It'll show map, generate some markers and run clustering
 */
public class MainActivity extends FragmentActivity {
    private ArrayList<MarkerOptions> markers = new ArrayList<MarkerOptions>();
    private Bitmap markerImage;
    private float oldZoom = 0;
    private GoogleMap map;
    private static final int INTERVAL = 25;
    private LinkedHashMap<Point, ArrayList<MarkerOptions>> clusters;
    private final double initLat1 = 40.462740;
    private final double initLng1 = 30.039572;
    private final double initLat2 = 48.462740;
    private final double initLng2 = 35.039572;
    private static final int MAP_ZOOM_LEVEL = 4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        markerImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);

        setContentView(R.layout.activity_main);

        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        map.getUiSettings().setMyLocationButtonEnabled(true);
        LatLng position = new LatLng(initLat2, initLng2);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, MAP_ZOOM_LEVEL));
        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (cameraPosition.zoom != oldZoom) {
                    try {
                        clusters = MarkersClusterizer.clusterMarkers(map, markers, INTERVAL);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                oldZoom = cameraPosition.zoom;
            }
        });
        createMarkers(map);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    /**
     * Markers generation
     *
     * @param map target instance of {@link com.google.android.gms.maps.GoogleMap}
     */
    private void createMarkers(GoogleMap map) {
        double initLat;
        double initLng;

        initLat = initLat1;
        initLng = initLng1;
        for (float i = 0; i < 2; i += 0.2) {
            LatLng pos = new LatLng(initLat + i, initLng + i);
            markers.add(new MarkerOptions().position(pos).icon(BitmapDescriptorFactory.fromBitmap(markerImage)));
        }

        initLat = initLat2;
        initLng = initLng2;
        for (float i = 0; i < 2; i += 0.2) {
            LatLng pos = new LatLng(initLat + i, initLng);
            markers.add(new MarkerOptions().position(pos).icon(BitmapDescriptorFactory.fromBitmap(markerImage)));
        }
        for (float i = 0; i < 2; i += 0.2) {
            LatLng pos = new LatLng(initLat, initLng + i);
            markers.add(new MarkerOptions().position(pos).icon(BitmapDescriptorFactory.fromBitmap(markerImage)));
        }

    }

}
