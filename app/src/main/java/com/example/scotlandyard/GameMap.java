package com.example.scotlandyard;

import android.content.Intent;
import android.content.res.Resources;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


public class GameMap extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    private static final String TAG = GameMap.class.getSimpleName();
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_navigation);

        Intent intent = getIntent();
        String getNickname = intent.getStringExtra(RegistrationActivty.passNickname);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getNickname);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        navigationView.setNavigationItemSelectedListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Fragment fragment = null;
        try {
            fragment = getSupportFragmentManager().findFragmentById(R.id.map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.map, fragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass;
        if (id == R.id.action_settings) {
            fragmentClass = GameMap.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.map, fragment).commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment;
        if (id == R.id.nav_game) {
            fragment = getSupportFragmentManager().findFragmentById(R.id.map);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.map, fragment).commit();
        } else {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        for (Point p : Points.getPoints()) {
            LatLng p_LatLng = new LatLng(p.getLatitude(), p.getLongitude());
            mMap.addMarker(new MarkerOptions().position(p_LatLng).title("" + (Points.getIndex(p) + 1)));
        }
        drawRoutes();
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

    private void drawRoutes() {
        drawByFoot();
    }

    private void drawByFoot() {
        for (Route r : Routes.getByFootRoutes()) {
            Point startPoint = Points.getPoints()[r.getStart_point() - 1];
            Point endPoint = Points.getPoints()[r.getEnd_point() - 1];
            LatLng start = new LatLng(startPoint.getLatitude(), startPoint.getLongitude());
            LatLng end = new LatLng(endPoint.getLatitude(), endPoint.getLongitude());
            PolylineOptions route = new PolylineOptions().add(start);
            if (r.getIntermediates() != null) {

            }
            route.add(end).color(Routes.getByFootColor()).width(Routes.ROUTE_WIDTH);
            mMap.addPolyline(route);
        }
    }

    //Creates a new Marker with given icon
    private Marker initializeMarker(int icon) {
        int position = (int) (Math.random() * Points.getPoints().length);
        LatLng latLng = new LatLng(Points.getLatFromP(position), Points.getLngfromP(position));
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng));
        marker.setIcon(BitmapDescriptorFactory.fromResource(icon));
        marker.setAnchor(0.5f,0.5f); //So the image is centered on the given position
        return marker;
    }

    //Places the marker on the position of point number
    public boolean positionMarker(Marker marker, int number) {
        if (marker == null || number < 1 || number >= Points.getPoints().length) return false;
        marker.setPosition(new LatLng(Points.getLatFromP(number), Points.getLngfromP(number)));
        return true;
        
    //Generates all Feeld-Marker
    private void generateFields(){
        for (int i = 1; i<points.length; i++) {
            putMarker(points[i].getFieldIcon(), i);
        }
    }
}
