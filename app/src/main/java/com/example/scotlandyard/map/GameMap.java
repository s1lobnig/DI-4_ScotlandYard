package com.example.scotlandyard.map;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.example.scotlandyard.control.Device;
import com.example.scotlandyard.control.GameInterface;
import com.example.scotlandyard.map.motions.LatLngInterpolator;
import com.example.scotlandyard.map.motions.MarkerAnimation;
import com.example.scotlandyard.map.motions.MovingLogic;
import com.example.scotlandyard.map.motions.RandomEvent;
import com.example.scotlandyard.map.motions.Move;
import com.example.scotlandyard.map.roadmap.Entry;
import com.example.scotlandyard.map.roadmap.PositionEntry;
import com.example.scotlandyard.map.roadmap.RoadMap;
import com.example.scotlandyard.map.roadmap.RoadMapDialog;
import com.example.scotlandyard.map.roadmap.TicketEntry;
import com.example.scotlandyard.messenger.Message;
import com.example.scotlandyard.messenger.Messenger;
import com.example.scotlandyard.Player;
import com.example.scotlandyard.PlayersOverview;
import com.example.scotlandyard.R;
import com.example.scotlandyard.Settings;
import com.example.scotlandyard.connection.Endpoint;
import com.example.scotlandyard.lobby.Game;
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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class GameMap extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GameInterface {

    private static Device device;

    private TextView pedestrianTickets;
    private TextView bicycleTickets;
    private TextView busTickets;
    private TextView taxiTickets;
    private TextView blackTickets;
    private TextView doubleTickets;

    private static final String TAG = GameMap.class.getSimpleName();
    private GoogleMap mMap;
    private static Player myPlayer;
    private boolean randomEventsEnabled;

    private static final int BY_FOOT_COLOR = Color.YELLOW;
    private static final int BUS_COLOR = Color.RED;
    private static final int BICYCLE_COLOR = Color.rgb(255, 164, 17);
    private static final int TAXI_DRAGAN_COLOR = Color.BLUE;


    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_navigation);

        //if game has not started yet
        if (device == null) {
            device = Device.getInstance();
            device.addGameObserver(this);
            device.setRoadMap(new RoadMap());
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(device.getNickname());
        setSupportActionBar(toolbar);

        pedestrianTickets = findViewById(R.id.pedestrianTicket);
        bicycleTickets = findViewById(R.id.bicycleTicket);
        busTickets = findViewById(R.id.busTicket);
        blackTickets = findViewById(R.id.blackTicket);
        taxiTickets = findViewById(R.id.taxiTicket);
        doubleTickets = findViewById(R.id.doubleTicket);

        //TODO: Set the fab to another color when message is received
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(GameMap.this, Messenger.class);
                mIntent.putExtra("USERNAME", device.getNickname());
                startActivity(mIntent);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Fragment fragment = null;
        try {
            fragment = getSupportFragmentManager().findFragmentById(R.id.map);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.map, fragment).commit();
    }

    @Override
    protected void onStop() {
        super.onStop();

        device.removeGameObserver();
    }

    /**
     * Method is called after onCreate and sets the menu-items (3-dot-menu, top
     * right corner)
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Selection-Handler which is triggered by selecting a menu-item
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settings = new Intent(this, Settings.class);
            startActivity(settings);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Selection-Handler which is triggered by selecting an item in the
     * navigation-drawer
     *
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment;
        Intent intent = null;
        if (id == R.id.nav_game) {
            fragment = getSupportFragmentManager().findFragmentById(R.id.map);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.map, fragment).commit();
        } else if (id == R.id.nav_overview) {
            intent = new Intent(this, PlayersOverview.class);
        } else if (id == R.id.nav_settings) {
            intent = new Intent(this, Settings.class);
        } else if (id == R.id.nav_road_map) {
            DialogFragment roadMapDialog = new RoadMapDialog();
            Bundle args = new Bundle();
            args.putSerializable("ROAD_MAP", device.getRoadMap());
            roadMapDialog.setArguments(args);
            roadMapDialog.show(getSupportFragmentManager(), "RoadMapDisplay");
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        if (intent != null) {
            startActivity(intent);
        }
        return true;
    }

    /**
     * Manipulates the map once available. This callback is triggered when the map
     * is ready to be used. This is where we can add markers or lines, add listeners
     * or move the camera. In this case, we just add a marker at the University
     * Klagenfurt. If Google Play services is not installed on the device, the user
     * will be prompted to install it inside the SupportMapFragment. This method
     * will only be triggered once the user has installed Google Play services and
     * returned to the app.
     */


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Restrict the map to a certain area
        final LatLngBounds mapBounds = new LatLngBounds(new LatLng(46.612225, 14.261226),
                new LatLng(46.623354, 14.271578));
        // Styling the map with a JSON-File
        try {
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.02);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mapBounds, width, height, padding));
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.setLatLngBoundsForCameraTarget(mapBounds);
        mMap.setMinZoomPreference(mMap.getCameraPosition().zoom);
        setFields();

        //if gmae has not started
        if (Device.isServer() && myPlayer == null) {
            device.setGame(ManageGameData.makeGame(Device.getLobby()));
            randomEventsEnabled = Device.getLobby().isRandomEvents();
            device.sendGame();
        }

        setupGame();
        visualizeTickets();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker field) {
                if (!ManageGameData.isPlayer(device.getGame(), field) && device.getGame().getRound() <= Game.getNumRounds()) {
                    if (!myPlayer.isMoved()) {
                        boolean isValid = isValidMove(field, myPlayer);
                        if (isValid) {
                            int r = (new Random()).nextInt(100) % 10;
                            Point newLocation = new Point(field.getPosition().latitude, field.getPosition().longitude);
                            if (Device.isServer()) {
                                if ((!myPlayer.isMrX() && device.getGame().isRoundMrX())  || (myPlayer.isMrX() && !device.getGame().isRoundMrX())){
                                    return false;
                                }
                                Point point = Points.getPoints()[Points.getIndex(newLocation)];
                                moveMarker(point, myPlayer, myPlayer.getIcon(), r);
                                myPlayer.setMoved(true);
                                tryNextRound();
                            }
                            device.send(new Move(myPlayer.getNickname(), Points.getIndex(newLocation), r));
                        }
                        return isValid;
                    } else {
                        // Toast to indicate that it is not your turn
                        Toast.makeText(GameMap.this, "Ein anderer Spieler ist noch nicht gezogen. Du musst noch warten.", Snackbar.LENGTH_LONG).show();
                    }
                }
                return false;
            }
        });
    }

    private boolean moveMarker(Point p, Player player, int playerIcon, int r) {
        if (randomEventsEnabled) {
            if (r < 3) {
                if (player.getPenalty() == 0) {
                    return moveWithRandomEvent(player, p, playerIcon, r);
                }
            }
        }
        return move(player, p, false, false, playerIcon);
    }

    private boolean isValidMove(Marker destination, Player player) {
        LatLng current = player.getMarker().getPosition();
        Point currentPoint = new Point(current.latitude, current.longitude);
        Point newLocation = new Point(destination.getPosition().latitude, destination.getPosition().longitude);
        Object[] routeToTake = Routes.getRoute(Points.getIndex(currentPoint), Points.getIndex(newLocation));
        if ((Boolean) routeToTake[0]) {
            boolean enoughTickets = ManageGameData.checkForValidTicket(player, (int) routeToTake[2]);
            if (!enoughTickets) {
                //Toast to indicate that player has not enough tickets for reachable field
                Toast.makeText(GameMap.this, "Nicht genügend Tickets", Snackbar.LENGTH_LONG).show();
                return false;
            }
            return true;
        }
        // Toast to indicate that the clicked location is not reachable from the current
        // location
        Toast.makeText(GameMap.this, "Feld nicht erreichbar", Snackbar.LENGTH_LONG).show();
        return false;
    }

    public boolean moveWithRandomEvent(Player player, Point p, int playerIcon, int randomEvent) {
        RandomEvent r = new RandomEvent();
        /*
        The randomEvent-paramter is already random generated - use it as id - random event is displayed equally on every device
         */
        r.setID(randomEvent);
        boolean goBack = false;
        boolean doNotGo = false;
        boolean randomRoute = false;
        boolean showMyToast = myPlayer.equals(player);

        if (r.getID() == 0) {
            if (showMyToast)
                Toast.makeText(GameMap.this, r.getText(), Snackbar.LENGTH_LONG).show();
            doNotGo = true;
        } else if (r.getID() == 1) {
            if (showMyToast)
                Toast.makeText(GameMap.this, r.getText(), Snackbar.LENGTH_LONG).show();
            goBack = true;
        } else if (r.getID() == 2) {
            if (showMyToast)
                Toast.makeText(GameMap.this, r.getText(), Snackbar.LENGTH_LONG).show();
            player.setPenalty(3);
        } else if (r.getID() == 3) {
            if (showMyToast)
                Toast.makeText(GameMap.this, r.getText(), Snackbar.LENGTH_LONG).show();
            randomRoute = true;
        }
        if (!doNotGo) {
            return move(player, p, goBack, randomRoute, playerIcon);
        }
        visualizeTickets();
        return false;
    }

    public boolean move(Player player, Point p, boolean goBack, boolean randomRoute, int playerIcon) {
        LatLng current = player.getMarker().getPosition();
        Point currentPoint = new Point(current.latitude, current.longitude);
        Point newLocation = p;
        Object[] routeToTake = Routes.getRoute(Points.getIndex(currentPoint), Points.getIndex(newLocation));
        if (randomRoute) {
            Routes.getRandomRoute(Points.getIndex(currentPoint), Points.getIndex(newLocation));
        }
        Route r = (Route) routeToTake[1];
        int[] iconAndTicket = MovingLogic.getIconAndTicket((int) routeToTake[2]);
        int icon = iconAndTicket[0];
        int ticket = iconAndTicket[1];
        visualizeTickets();
        if (player.isMrX() && player.equals(myPlayer)) {
            int lastTurn = device.getRoadMap().getNumberOfEntries();
            Entry entry = MovingLogic.getRoadMapEntry(lastTurn, newLocation, ticket);
            if (Device.isServer()) {
                device.getRoadMap().addEntry(entry);
            }
            device.send(entry);
        }
        if (!(player.getPenalty() > 0 && icon == R.drawable.bicycle)) {
            if (player.getPenalty() > 0)
                player.decreasePenalty();
            if (r.getIntermediates() != null) {
                player.getMarker().setIcon(BitmapDescriptorFactory.fromResource(icon));
                Object[] routeSliceTimings = MovingLogic.getRouteSlicesAndTimings(r, Points.getIndex(currentPoint) + 1);
                ArrayList<LatLng> routePoints = (ArrayList) routeSliceTimings[0];
                ArrayList<Float> timeSlices = (ArrayList) routeSliceTimings[1];
                LatLng finalPos = p.getLatLng();
                if (goBack) {
                    // if random event "Go Back" then...
                    MovingLogic.createGoBackRoute(timeSlices, routePoints, p);
                    finalPos = player.getMarker().getPosition();
                }
                MovingLogic.runMarkerAnimation(player, routePoints, timeSlices, finalPos, icon, playerIcon);
            } else {
                if (!goBack) {
                    MovingLogic.runMarkerAnimation(player, null, null, p.getLatLng(), icon, playerIcon);
                } else {
                    ArrayList[] goBackRouteAndSlices = MovingLogic.createGoBackRoute(p.getLatLng());
                    ArrayList<Float> timeSlices = goBackRouteAndSlices[0];
                    ArrayList<LatLng> routePoints = goBackRouteAndSlices[1];
                    MovingLogic.runMarkerAnimation(player, routePoints, timeSlices, player.getMarker().getPosition(), icon, playerIcon);
                }
            }
        } else {
            Toast.makeText(GameMap.this, "Das Fahrrad ist noch nicht verfügbar!", Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;

    }

    public void visualizeTickets() {
        if (myPlayer.isMrX()) {
            pedestrianTickets.setText("∞");
            bicycleTickets.setText("∞");
            busTickets.setText("∞");
            blackTickets.setText(myPlayer.getTickets().get(R.string.BLACK_TICKET_KEY).toString());
            taxiTickets.setText(myPlayer.getTickets().get(R.string.TAXI_TICKET_KEY).toString());
            doubleTickets.setText(myPlayer.getTickets().get(R.string.DOUBLE_TICKET_KEY).toString());
        } else {
            pedestrianTickets.setText(myPlayer.getTickets().get(R.string.PEDESTRIAN_TICKET_KEY).toString());
            bicycleTickets.setText(myPlayer.getTickets().get(R.string.BICYCLE_TICKET_KEY).toString());
            busTickets.setText(myPlayer.getTickets().get(R.string.BUS_TICKET_KEY).toString());
            blackTickets.setVisibility(View.INVISIBLE);
            taxiTickets.setVisibility(View.INVISIBLE);
            doubleTickets.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Adds all Points to the map and calls drawRoutes() at the end
     */
    private void setFields() {
        for (Point p : Points.getPoints()) {
            LatLng p_LatLng = p.getLatLng();
            Drawable myDrawable = getResources().getDrawable(p.getIcon());
            Bitmap bmp = ((BitmapDrawable) myDrawable).getBitmap();
            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bmp);
            mMap.addMarker(new MarkerOptions().position(p_LatLng).icon(icon).anchor(0.5f, 0.5f));
        }
        drawRoutes();
    }

    /**
     * Draws all Routes
     */
    private void drawRoutes() {
        drawByFoot();
        drawByBicycle();
        drawByBus();
        drawByTaxiDragan();
    }

    /**
     * Draws all "BUS"-Routes
     */
    private void drawByBus() {
        for (Route r : Routes.getBusRoutes()) {
            addRoute(r, BUS_COLOR);
        }
    }

    /**
     * Draws all "TAXI DRAGAN"-Routes
     */
    private void drawByTaxiDragan() {
        for (Route r : Routes.getTaxiDraganRoutes()) {
            addRoute(r, TAXI_DRAGAN_COLOR);
        }
    }

    /**
     * Draws all "FOOT"-Routes
     */
    private void drawByFoot() {
        for (Route r : Routes.getByFootRoutes()) {
            addRoute(r, BY_FOOT_COLOR);
        }
    }

    /**
     * Draws all "BICYCLE"-Routes
     */
    private void drawByBicycle() {
        for (Route r : Routes.getBicycleRoutes()) {
            addRoute(r, BICYCLE_COLOR);
        }
    }

    /**
     * Adds a polyline to the map
     *
     * @param r     A Route-Object which contains start- & end-point and, if
     *              necessary also intermediate points for drawing the route
     * @param color An int-value which represents the color of the route to be drawn
     */
    private void addRoute(Route r, int color) {
        Point startPoint = Points.getPoints()[r.getStartPoint() - 1];
        Point endPoint = Points.getPoints()[r.getEndPoint() - 1];
        LatLng start = startPoint.getLatLng();
        LatLng end = endPoint.getLatLng();
        PolylineOptions route = new PolylineOptions().add(start);
        if (r.getIntermediates() != null) {
            for (Point p : r.getIntermediates()) {
                route.add(p.getLatLng());
            }
        }
        route.add(end).color(color).width(Routes.ROUTE_WIDTH);
        mMap.addPolyline(route);
    }

    private void setupGame() {
        for (Player p : device.getGame().getPlayers()) {
            if (p.isActive()) {
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(p.getPosition().getLatLng())
                        .icon(BitmapDescriptorFactory.fromResource(p.getIcon()));
                p.setMarker(mMap.addMarker(markerOptions));
                p.getMarker().setTitle(p.getNickname());
            }
            if (p.getNickname().equals(device.getNickname())) {
                myPlayer = p;
            }
        }
        randomEventsEnabled = device.getGame().isRandomEventsEnabled();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPlayer.getPosition().getLatLng(), 16f), 3000, null);
    }

    private void tryNextRound() {
        int result = ManageGameData.tryNextRound(device.getGame());
        if (result == 1) {
            device.send(new MapNotification("NEXT_ROUND"));
            Toast.makeText(GameMap.this, "Runde " + device.getGame().getRound(), Snackbar.LENGTH_LONG).show();
        } else if (result == 0) {
            device.send(new MapNotification("END MisterX")); //MisterX hat gewonnen
            Toast.makeText(GameMap.this, "MisterX hat gewonnen", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void updateMove(Move move) {
        Player player = ManageGameData.findPlayer(device.getGame(), move.getNickname());
        int field = move.getField();
        Point point = Points.getPoints()[field];

        moveMarker(point, player, player.getIcon(), move.getRandomEventTrigger());
    }

    @Override
    public void showDisconnected(Endpoint endpoint) {
        if (Device.isServer()) {
            Toast.makeText(GameMap.this, "Verbindung zu Server verlohren!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(GameMap.this, "Verbindung zu Player " + endpoint.getName() + " verlohren!", Toast.LENGTH_LONG).show();
            //TODO: Server lost!
        }
    }

    @Override
    public void showSendingFailed(Object object) {
        String notification = "Objekt";
        if (object instanceof Game) {
            notification = "Gamedaten";
        } else if (object instanceof Move) {
            notification = "Zug";
        }
        Toast.makeText(GameMap.this, notification + "konnte nicht gesendet werden!", Toast.LENGTH_LONG).show();
        //TODO give possibility to sync the game again
    }
}
