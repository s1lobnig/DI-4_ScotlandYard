package com.example.scotlandyard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

import java.util.ArrayList;


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
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(map_bounds, width, height, padding));
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.setLatLngBoundsForCameraTarget(map_bounds);
        mMap.setMinZoomPreference(mMap.getCameraPosition().zoom);
        setFields();
        final Marker player1 = initializeMarker(R.drawable.player1);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                LatLng current = player1.getPosition();
                Point currentPoint = new Point(current.latitude, current.longitude);
                Point newLocation = new Point(marker.getPosition().latitude, marker.getPosition().longitude);
                Object[] routeToTake = Routes.getRoute(Points.getIndex(currentPoint), Points.getIndex(newLocation));
                boolean isValid = (Boolean) routeToTake[0];
                if (isValid) {
                    Route r = (Route) routeToTake[1];
                    Log.d("ROUTE", r.toString());
                    String txt = "";
                    int icon = -1;
                    int vehicle = (int) routeToTake[2];
                    switch (vehicle) {
                        case 0:
                            icon = R.drawable.pedestrian;
                            txt = "foot route";
                            break;
                        case 1:
                            icon = R.drawable.bicycle;
                            txt = "bicycle route";
                            break;
                        case 2:
                            icon = R.drawable.bus;
                            txt = "bus route";
                            break;
                        case 3:
                            icon = R.drawable.taxi;
                            txt = "taxi route";
                            break;
                        case -1:
                            break;
                    }
                    Toast.makeText(GameMap.this, txt, Snackbar.LENGTH_LONG).show();

                    int animation_duration = 3000;
                    if (r.getIntermediates() != null) {
                        double routeLength = r.getLength();
                        int duration;
                        player1.setIcon(BitmapDescriptorFactory.fromResource(icon));
                        final ArrayList<ObjectAnimator> animations = new ArrayList<>();
                        if (Points.getIndex(currentPoint) == r.getStart_point() - 1) {
                            for (int i = 0; i <= r.getIntermediates().length; i++) {
                                double x1;
                                double y1;
                                double x2;
                                double y2;
                                if (i == 0) {
                                    x1 = Points.POINTS[r.getStart_point() - 1].getLatitude();
                                    y1 = Points.POINTS[r.getStart_point() - 1].getLongitude();
                                } else {
                                    x1 = r.getIntermediates()[i - 1].getLatitude();
                                    y1 = r.getIntermediates()[i - 1].getLongitude();
                                }
                                if (i == r.getIntermediates().length) {
                                    x2 = Points.POINTS[r.getEnd_point() - 1].getLatitude();
                                    y2 = Points.POINTS[r.getEnd_point() - 1].getLongitude();
                                } else {
                                    x2 = r.getIntermediates()[i].getLatitude();
                                    y2 = r.getIntermediates()[i].getLongitude();
                                }
                                LatLng intermediate = new LatLng(x2, x1);
                                duration = (int) (animation_duration * ((Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2))) / routeLength));
                                Log.d("ANIMATION", "" + duration);
                                animations.add(MarkerAnimation.animateMarkerToICS(player1, intermediate, new LatLngInterpolator.Linear(), duration, icon));
                            }
                        } else {
                            for (int i = r.getIntermediates().length; i >= 0; i--) {
                                double x1;
                                double y1;
                                double x2;
                                double y2;
                                if (i == r.getIntermediates().length) {
                                    x1 = Points.POINTS[r.getEnd_point() - 1].getLatitude();
                                    y1 = Points.POINTS[r.getEnd_point() - 1].getLongitude();
                                } else {
                                    x1 = r.getIntermediates()[i].getLatitude();
                                    y1 = r.getIntermediates()[i].getLongitude();
                                }
                                if (i == 0) {
                                    x2 = Points.POINTS[r.getStart_point() - 1].getLatitude();
                                    y2 = Points.POINTS[r.getStart_point() - 1].getLongitude();
                                } else {
                                    x2 = r.getIntermediates()[i - 1].getLatitude();
                                    y2 = r.getIntermediates()[i - 1].getLongitude();
                                }
                                LatLng intermediate = new LatLng(x2, x1);
                                duration = (int) (animation_duration * ((Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2))) / routeLength));
                                Log.d("ANIMATION", "" + duration);
                                animations.add(MarkerAnimation.animateMarkerToICS(player1, intermediate, new LatLngInterpolator.Linear(), duration, icon));
                            }
                        }
                        for (int i = 0; i < animations.size(); i++) {
                            final int finalI = i;
                            animations.get(i).addListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    Log.d("ANIMATION", "" + finalI);
                                    if (finalI != animations.size() - 1) {
                                        animations.get(finalI + 1).start();
                                    } else {
                                        player1.setPosition(marker.getPosition());
                                        player1.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.player1));
                                    }
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            });
                        }
                        animations.get(0).start();
                    } else {
                        MarkerAnimation.animateMarkerToGB(player1, marker.getPosition(), new LatLngInterpolator.Spherical(), animation_duration, icon);
                    }
                    return true;
                }
                Toast.makeText(GameMap.this, "Unreachable Point :(", Snackbar.LENGTH_LONG).show();
                return false;
            }
        });
    }

    private void setFields() {
        for (Point p : Points.getPoints()) {
            LatLng p_LatLng = new LatLng(p.getLatitude(), p.getLongitude());
            Drawable myDrawable = getResources().getDrawable(p.getIcon());
            Bitmap bmp = ((BitmapDrawable) myDrawable).getBitmap();
            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bmp);
            mMap.addMarker(
                    new MarkerOptions()
                            .position(p_LatLng)
                            .icon(icon)
                            .anchor(0.5f, 0.5f)
            );
        }
        drawRoutes();
    }

    private void drawRoutes() {
        drawByFoot();
        drawByBicycle();
        drawByBus();
        drawByTaxiDragan();
    }

    private void drawByBus() {
        for (Route r : Routes.getBusRoutes()) {
            addRoute(r, Routes.getBusColor());
        }
    }

    private void drawByTaxiDragan() {
        for (Route r : Routes.getTaxiDraganRoutes()) {
            addRoute(r, Routes.getTaxiDraganColor());
        }
    }

    private void drawByFoot() {
        for (Route r : Routes.getByFootRoutes()) {
            addRoute(r, Routes.getByFootColor());
        }
    }

    private void drawByBicycle() {
        for (Route r : Routes.getBicycleRoutes()) {
            addRoute(r, Routes.getBicycleColor());
        }
    }

    private void addRoute(Route r, int color) {
        Point startPoint = Points.getPoints()[r.getStart_point() - 1];
        Point endPoint = Points.getPoints()[r.getEnd_point() - 1];
        LatLng start = new LatLng(startPoint.getLatitude(), startPoint.getLongitude());
        LatLng end = new LatLng(endPoint.getLatitude(), endPoint.getLongitude());
        PolylineOptions route = new PolylineOptions().add(start);
        if (r.getIntermediates() != null) {
            for (Point p : r.getIntermediates()) {
                route.add(new LatLng(p.getLatitude(), p.getLongitude()));
            }
        }
        route.add(end).color(color).width(Routes.ROUTE_WIDTH);
        mMap.addPolyline(route);
    }

    //Creates a new Marker with given icon
    private Marker initializeMarker(int icon) {
        int position = (int) (Math.random() * Points.getPoints().length);
        LatLng latLng = new LatLng(Points.getLatFromP(position), Points.getLngfromP(position));
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng));
        marker.setIcon(BitmapDescriptorFactory.fromResource(icon));
//        marker.setAnchor(0.5f, 0.5f); //So the image is centered on the given position
        return marker;
    }

    //Places the marker on the position of point number
    public boolean positionMarker(Marker marker, int number) {
        if (marker == null || number < 1 || number >= Points.getPoints().length) return false;
        marker.setPosition(new LatLng(Points.getLatFromP(number), Points.getLngfromP(number)));
        return true;
    }
}
