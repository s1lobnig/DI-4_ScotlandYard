package com.example.scotlandyard;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

public class GameMap extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = GameMap.class.getSimpleName();
    private GoogleMap mMap;
    private Points points = new Points();

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
        /*
            "north":  46.623354,
            "east":   14.271578,
            "south":  46.612225,
            "west":   14.261226
         */
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
        int padding = (int) (width * 0.20);
        // Add a marker at the University Klagenfurt and move the camera
        LatLng aau = new LatLng(46.616389, 14.265);
        float zoom_factor = 16f;
        mMap.addMarker(new MarkerOptions().position(aau).title("Marker in AAU"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(map_bounds, width, height, padding));
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.setLatLngBoundsForCameraTarget(map_bounds);
        mMap.setMinZoomPreference(mMap.getCameraPosition().zoom);

        initializeMarker(R.drawable.player1);
    }

    //Creates a new Marker with given icon
    private Marker initializeMarker(int icon){
        int position = (int) (Math.random()*points.getDots().length);
        LatLng latLng = new LatLng(points.getXfromP(position), points.getYfromP(position));
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng));
        marker.setIcon(BitmapDescriptorFactory.fromResource(icon));
        return marker;
    }

    //Places the marker on the position of point number
    public boolean positionMarker(Marker marker, int number){
        if(marker == null || number<1 || number>=points.getDots().length) return false;
        marker.setPosition(new LatLng(points.getXfromP(number), points.getYfromP(number)));
        return true;
    }
}
