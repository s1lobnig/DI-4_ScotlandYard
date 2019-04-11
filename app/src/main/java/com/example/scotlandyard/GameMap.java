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
        int padding = (int) (width * 0.02);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(map_bounds, width, height, padding));
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.setLatLngBoundsForCameraTarget(map_bounds);
        mMap.setMinZoomPreference(mMap.getCameraPosition().zoom);

        generateFeelds();
        initializeMarker(R.drawable.player1);
    }

    //Creates a new Marker with given icon
    private Marker initializeMarker(int icon){
        int position = (int) (Math.random()*points.length);
        return putMarker(icon, position);
    }

    //Places the marker on the position of point number
    public boolean positionMarker(Marker marker, int position){
        if(marker == null || position<1 || position>=points.length) return false;
        marker.setPosition(new LatLng(points[position].getX(), points[position].getY()));
        return true;
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
    private void generateFeelds(){
        putMarker(R.drawable.feeld1, 1);
        putMarker(R.drawable.feeld2, 2);
        putMarker(R.drawable.feeld3, 3);
        putMarker(R.drawable.feeld4, 4);
        putMarker(R.drawable.feeld5, 5);
        putMarker(R.drawable.feeld6, 6);
        putMarker(R.drawable.feeld7, 7);
        putMarker(R.drawable.feeld8, 8);
        putMarker(R.drawable.feeld9, 9);
        putMarker(R.drawable.feeld10, 10);
        putMarker(R.drawable.feeld11, 11);
        putMarker(R.drawable.feeld12, 12);
        putMarker(R.drawable.feeld13, 13);
        putMarker(R.drawable.feeld14, 14);
        putMarker(R.drawable.feeld15, 15);
        putMarker(R.drawable.feeld16, 16);
        putMarker(R.drawable.feeld17, 17);
        putMarker(R.drawable.feeld18, 18);
        putMarker(R.drawable.feeld19, 19);
        putMarker(R.drawable.feeld20, 20);
        putMarker(R.drawable.feeld21, 21);
        putMarker(R.drawable.feeld22, 22);
        putMarker(R.drawable.feeld23, 23);
        putMarker(R.drawable.feeld24, 24);
        putMarker(R.drawable.feeld25, 25);
        putMarker(R.drawable.feeld26, 26);
        putMarker(R.drawable.feeld27, 27);
        putMarker(R.drawable.feeld28, 28);
        putMarker(R.drawable.feeld29, 29);
        putMarker(R.drawable.feeld30, 30);
        putMarker(R.drawable.feeld31, 31);
        putMarker(R.drawable.feeld32, 32);
        putMarker(R.drawable.feeld33, 33);
        putMarker(R.drawable.feeld34, 34);
        putMarker(R.drawable.feeld35, 35);
        putMarker(R.drawable.feeld36, 36);
        putMarker(R.drawable.feeld37, 37);
        putMarker(R.drawable.feeld38, 38);
        putMarker(R.drawable.feeld39, 39);
        putMarker(R.drawable.feeld40, 40);
        putMarker(R.drawable.feeld41, 41);
        putMarker(R.drawable.feeld42, 42);
        putMarker(R.drawable.feeld43, 43);
        putMarker(R.drawable.feeld44, 44);
        putMarker(R.drawable.feeld45, 45);
        putMarker(R.drawable.feeld46, 46);
        putMarker(R.drawable.feeld47, 47);
        putMarker(R.drawable.feeld48, 48);
        putMarker(R.drawable.feeld49, 49);
        putMarker(R.drawable.feeld50, 50);
        putMarker(R.drawable.feeld51, 51);
        putMarker(R.drawable.feeld52, 52);
        putMarker(R.drawable.feeld53, 53);
        putMarker(R.drawable.feeld54, 54);
        putMarker(R.drawable.feeld55, 55);
        putMarker(R.drawable.feeld56, 56);
        putMarker(R.drawable.feeld57, 57);
        putMarker(R.drawable.feeld58, 58);
        putMarker(R.drawable.feeld59, 59);
        putMarker(R.drawable.feeld60, 60);
        putMarker(R.drawable.feeld61, 61);
        putMarker(R.drawable.feeld62, 62);
        putMarker(R.drawable.feeld63, 63);
        putMarker(R.drawable.feeld64, 64);
        putMarker(R.drawable.feeld65, 65);
        putMarker(R.drawable.feeld66, 66);
        putMarker(R.drawable.feeld67, 67);
        putMarker(R.drawable.feeld68, 68);
        putMarker(R.drawable.feeld69, 69);
        putMarker(R.drawable.feeld70, 70);
        putMarker(R.drawable.feeld71, 71);
        putMarker(R.drawable.feeld72, 72);
        putMarker(R.drawable.feeld73, 73);
        putMarker(R.drawable.feeld74, 74);
        //putMarker(R.drawable.feeld75, 75);

    }
}
