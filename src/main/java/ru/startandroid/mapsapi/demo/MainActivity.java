
package ru.startandroid.mapsapi.demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import ru.startandroid.mapsapi.R;
import ru.startandroid.mapsapi.cluster.MarkersClusterizer;

import java.util.ArrayList;

/**
 * Demo activity. It'll show map, generate some markers and run clustering
 */
public class MainActivity extends FragmentActivity {
    private ArrayList<MarkerOptions> markers = new ArrayList<MarkerOptions>();
    private Bitmap markerImage;
    private float oldZoom = 0;
    private GoogleMap map;
    private static final int INTERVAL = 25;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        markerImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);

        setContentView(R.layout.activity_main);

        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (cameraPosition.zoom != oldZoom) {
                    MarkersClusterizer.clusterMarkers(map, markers, INTERVAL);
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

        initLat = 40.462740;
        initLng = 30.039572;
        for (float i = 0; i < 2; i += 0.2) {
            LatLng pos = new LatLng(initLat + i, initLng + i);
            markers.add(new MarkerOptions().position(pos).icon(BitmapDescriptorFactory.fromBitmap(markerImage)));
        }

        initLat = 48.462740;
        initLng = 35.039572;
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
