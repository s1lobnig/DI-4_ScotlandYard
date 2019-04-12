package com.example.scotlandyard;

import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;

public class GameMap extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = GameMap.class.getSimpleName();
    private GoogleMap mMap;
    private Point[] points = new Points().getDots();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker at the University Klagenfurt.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        final LatLngBounds map_bounds = new LatLngBounds(
                new LatLng(46.612225, 14.261226),
                new LatLng(46.623354, 14.271578)
        );
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        //get width and height to current display screen
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        // 20% padding
        int padding = (int) (width * 0.02);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(map_bounds, width, height, padding));
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.setLatLngBoundsForCameraTarget(map_bounds);
        mMap.setMinZoomPreference(mMap.getCameraPosition().zoom);

        generateFields();
        final Marker player1 = initializeMarker(R.drawable.player1);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //ToDo check if Position is reachable from players current position
                player1.setPosition(marker.getPosition());
                return true;
            }
        });
    }

    //Creates a new Marker with given icon
    private Marker initializeMarker(int icon){
        Random r = new Random();
        int position = r.nextInt(points.length-1);
        return putMarker(icon, position);
    }

    //Creates a new Marker with given Icon at given position
    public Marker putMarker(int icon, int position){
        LatLng latLng = new LatLng(points[position].getX(), points[position].getY());
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng));
        marker.setIcon(BitmapDescriptorFactory.fromResource(icon));
        marker.setAnchor(0.5f,0.5f); //So the image is centered on the given position
        return marker;
    }

    //Generates all Feeld-Marker
    private void generateFields(){
        for (int i = 1; i<points.length; i++) {
            putMarker(points[i].getFieldIcon(), i);
        }
    }
}
