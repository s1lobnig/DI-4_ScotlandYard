package com.example.scotlandyard.map;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.example.scotlandyard.control.GameInterface;
import com.example.scotlandyard.map.motions.LatLngInterpolator;
import com.example.scotlandyard.map.motions.MarkerAnimation;
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
import com.example.scotlandyard.connection.ClientInterface;
import com.example.scotlandyard.connection.ClientService;
import com.example.scotlandyard.connection.Endpoint;
import com.example.scotlandyard.connection.ServerInterface;
import com.example.scotlandyard.connection.ServerService;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameMap extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GameInterface {

    private static ServerService serverService;
    private static ClientService clientService;
    private static ManageGameData manageGame;

    private boolean isServer;
    private static String logTag;
    private static String nickname;
    private static final String TAG = GameMap.class.getSimpleName();
    private GoogleMap mMap;
    private static int playerPenaltay = 0;
    private Player myPlayer;

    private RoadMap roadMap;
    private boolean randomEventsEnabled;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_navigation);

        //if game has not started yet
        if(nickname == null) {
            Intent intent = getIntent();
            nickname = intent.getStringExtra("USERNAME");
            isServer = intent.getBooleanExtra("IS_SERVER", true);
            manageGame = new ManageGameData();
            /*
            if (isServer) {
                manageGame.game = ((Game) intent.getSerializableExtra("GAME"));
                serverService = ServerService.getInstance();
                serverService.setServer(this);
                logTag = "SERVER_SERVICE";
            } else {
                clientService = ClientService.getInstance();
                clientService.setClient(this);
                logTag = "CLIENT_SERVICE";
            }*/
            this.roadMap = new RoadMap();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(nickname);
        setSupportActionBar(toolbar);

        //TODO: Set the fab to another color when message is received
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(GameMap.this, Messenger.class);
                mIntent.putExtra("USERNAME", nickname);
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
            args.putSerializable("ROAD_MAP", roadMap);
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
        if (isServer && myPlayer == null) {
            manageGame.givePlayerPositionAndIcon();
            serverService.send(manageGame.game);
        }
        if(manageGame.game != null) {
            setupGame();
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker field) {
                if (!manageGame.isPlayer(field)) {
                    if (!myPlayer.isMoved()) {
                        boolean isValid = isValidMove(field, myPlayer);
                        if (isValid) {
                            Point newLocation = new Point(field.getPosition().latitude, field.getPosition().longitude);
                            if (isServer) {
                                Point point = Points.getPoints()[Points.getIndex(newLocation)];
                                moveMarker(point, myPlayer, myPlayer.getIcon());
                                serverService.send(new Move(myPlayer.getNickname(), Points.getIndex(newLocation)));
                                myPlayer.setMoved(true);
                                tryNextRound();
                            } else {
                                clientService.send(new Move(myPlayer.getNickname(), Points.getIndex(newLocation)));
                            }
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



    private boolean moveWithRandomEvent(Player player, Point p, int playerIcon) {
        RandomEvent r = new RandomEvent();
        boolean goBack = false;
        boolean doNotGo = false;
        boolean randomRoute = false;

        if (r.getID() == 0) {
            Toast.makeText(GameMap.this, r.getText(), Snackbar.LENGTH_LONG).show();
            doNotGo = true;
        } else if (r.getID() == 1) {
            Toast.makeText(GameMap.this, r.getText(), Snackbar.LENGTH_LONG).show();
            goBack = true;
        } else if (r.getID() == 2) {
            Toast.makeText(GameMap.this, r.getText(), Snackbar.LENGTH_LONG).show();
            playerPenaltay = 3;
        } else if (r.getID() == 3) {
            Toast.makeText(GameMap.this, r.getText(), Snackbar.LENGTH_LONG).show();
            randomRoute = true;
        }
        if (!doNotGo) {
            return move(player, p, goBack, randomRoute, playerIcon);
        }
        return false;
    }

    private boolean moveMarker(Point p, Player player, int playerIcon) {
        if (randomEventsEnabled) {
            int r = (new Random()).nextInt(100) % 10;
            if (false && r < 3) {
                if (playerPenaltay == 0) {
                    return moveWithRandomEvent(player, p, playerIcon);
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
            boolean enoughTickets = manageGame.checkForValidTicket(player, (int) routeToTake[2]);
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

    private boolean move(Player player, Point p, boolean goBack, boolean randomRoute, int playerIcon) {
        LatLng current = player.getMarker().getPosition();
        Point currentPoint = new Point(current.latitude, current.longitude);
        Point newLocation = p;
        Object[] routeToTake = Routes.getRoute(Points.getIndex(currentPoint), Points.getIndex(newLocation));
        boolean isValid = (Boolean) routeToTake[0];
        // if the route would be valid but there is the randowm event "verfahren", then...
        // Note, this does not work at the moment!
        if (isValid && randomRoute) {
            routeToTake = Routes.getRandomRoute(Points.getIndex(currentPoint), Points.getIndex(newLocation));
        }
        if (isValid) {
            Route r = (Route) routeToTake[1];
            int icon;
            int ticket;
            int vehicle = (int) routeToTake[2];
            switch (vehicle) {
                case 0:
                    icon = R.drawable.pedestrian;
                    ticket = R.drawable.ticket_yellow;
                    break;
                case 1:
                    icon = R.drawable.bicycle;
                    ticket = R.drawable.ticket_orange;
                    break;
                case 2:
                    icon = R.drawable.bus;
                    ticket = R.drawable.ticket_red;
                    break;
                case 3:
                    icon = R.drawable.taxi;
                    ticket = R.drawable.ticket_black;
                    break;
                default:
                    icon = -1;
                    ticket = -1;
            }
            if (myPlayer.isMrX()) {
                Entry entry;
                int lastTurn = roadMap.getNumberOfEntries();
                if (lastTurn == 2 || lastTurn == 6 || lastTurn == 11) {
                    entry = new PositionEntry(lastTurn + 1, Points.getIndex(newLocation) + 1);
                } else {
                    entry = new TicketEntry(lastTurn + 1, ticket);
                }
                if (isServer) {
                    roadMap.addEntry(entry);
                    serverService.send(entry);
                } else {
                    clientService.send(entry);
                }
            }
            int animationDuration = 3000;
            if (!(playerPenaltay > 0 && icon == R.drawable.bicycle)) {
                if (playerPenaltay > 0)
                    playerPenaltay--;
                if (r.getIntermediates() != null) {
                    player.getMarker().setIcon(BitmapDescriptorFactory.fromResource(icon));
                    Object[] routeSliceTimings = getRouteSlicesAndTimings(r, animationDuration, Points.getIndex(currentPoint) + 1);
                    final ArrayList<LatLng> routePoints = (ArrayList) routeSliceTimings[0];
                    final ArrayList<Float> timeSlices = (ArrayList) routeSliceTimings[1];
                    LatLng finalPos = p.getLatLng();
                    if (goBack) {
                        // if random event "Go Back" then...
                        int size = timeSlices.size();
                        for (int i = size - 1; i >= 0; i--) {
                            timeSlices.add(timeSlices.get(i));
                        }
                        size = routePoints.size();
                        routePoints.add(p.getLatLng());
                        for (int i = size - 1; i >= 0; i--) {
                            routePoints.add(routePoints.get(i));
                        }
                        finalPos = player.getMarker().getPosition();
                    }
                    MarkerAnimation.moveMarkerToTarget(player.getMarker(), routePoints, timeSlices, finalPos, new LatLngInterpolator.Linear(), icon, false, GameMap.this, playerIcon);
                } else {
                    if (!goBack) {
                        MarkerAnimation.moveMarkerToTarget(player.getMarker(), p.getLatLng(), new LatLngInterpolator.Linear(), animationDuration, icon, playerIcon);
                    } else {
                        // if rand event, then...
                        ArrayList<Float> timeSlices = new ArrayList<>();
                        timeSlices.add((float) animationDuration);
                        timeSlices.add((float) animationDuration);
                        ArrayList<LatLng> routePoints = new ArrayList<>();
                        routePoints.add(p.getLatLng());
                        MarkerAnimation.moveMarkerToTarget(player.getMarker(), routePoints, timeSlices, player.getMarker().getPosition(), new LatLngInterpolator.Linear(), icon, true, GameMap.this, playerIcon);
                    }
                }
            } else {
                Toast.makeText(GameMap.this, "Das Fahrrad ist noch nicht verfügbar!", Snackbar.LENGTH_LONG).show();
            }
            return true;
        }
        // Toast to indicate that the clicked location is not reachable from the current location
        Toast.makeText(GameMap.this, "Unreachable Point :(", Snackbar.LENGTH_LONG).show();
        return false;
    }

    /**
     * @param r                 Route-Object
     * @param animationDuration Duration of the whole animation
     * @param startPos          Current location-index
     * @return { ArrayList<LatLang> in the correct order (either original (if
     * current position = route.StartPos) or revered) ArrayList<Float> which
     * contains the animation-duration-slices according to the
     * route-part-length }
     */
    private Object[] getRouteSlicesAndTimings(Route r, int animationDuration, int startPos) {
        if (startPos == r.getStartPoint()) {
            return regularOrder(r, animationDuration);
        } else {
            return reverseOrder(r, animationDuration);
        }
    }

    private Object[] reverseOrder(Route r, int animationDuration) {
        float duration;
        ArrayList<Float> timeSlices = new ArrayList<>();
        ArrayList<LatLng> routePoints = new ArrayList<>();
        for (int i = r.getIntermediates().length; i >= 0; i--) {
            double x1;
            double y1;
            double x2;
            double y2;
            if (i == r.getIntermediates().length) {
                x1 = Points.POINTS[r.getEndPoint() - 1].getLatitude();
                y1 = Points.POINTS[r.getEndPoint() - 1].getLongitude();
            } else {
                x1 = r.getIntermediates()[i].getLatitude();
                y1 = r.getIntermediates()[i].getLongitude();
            }
            if (i == 0) {
                x2 = Points.POINTS[r.getStartPoint() - 1].getLatitude();
                y2 = Points.POINTS[r.getStartPoint() - 1].getLongitude();
            } else {
                x2 = r.getIntermediates()[i - 1].getLatitude();
                y2 = r.getIntermediates()[i - 1].getLongitude();
            }
            LatLng intermediate = new LatLng(x2, y2);
            duration = (float) (animationDuration
                    * ((Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2))) / r.getLength()));
            timeSlices.add(duration);
            if (i != 0) {
                routePoints.add(intermediate);
            }
        }
        return new Object[]{routePoints, timeSlices};
    }

    private Object[] regularOrder(Route r, int animationDuration) {
        float duration;
        ArrayList<Float> timeSlices = new ArrayList<>();
        ArrayList<LatLng> routePoints = new ArrayList<>();
        for (int i = 0; i <= r.getIntermediates().length; i++) {
            double x1;
            double y1;
            double x2;
            double y2;
            if (i == 0) {
                x1 = Points.POINTS[r.getStartPoint() - 1].getLatitude();
                y1 = Points.POINTS[r.getStartPoint() - 1].getLongitude();
            } else {
                x1 = r.getIntermediates()[i - 1].getLatitude();
                y1 = r.getIntermediates()[i - 1].getLongitude();
            }
            if (i == r.getIntermediates().length) {
                x2 = Points.POINTS[r.getEndPoint() - 1].getLatitude();
                y2 = Points.POINTS[r.getEndPoint() - 1].getLongitude();
            } else {
                x2 = r.getIntermediates()[i].getLatitude();
                y2 = r.getIntermediates()[i].getLongitude();
            }
            LatLng intermediate = new LatLng(x2, y2);
            duration = (float) (animationDuration
                    * ((Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2))) / r.getLength()));
            timeSlices.add(duration);
            if (i != r.getIntermediates().length) {
                routePoints.add(intermediate);
            }
        }
        return new Object[]{routePoints, timeSlices};
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
            addRoute(r, Routes.getBusColor());
        }
    }

    /**
     * Draws all "TAXI DRAGAN"-Routes
     */
    private void drawByTaxiDragan() {
        for (Route r : Routes.getTaxiDraganRoutes()) {
            addRoute(r, Routes.getTaxiDraganColor());
        }
    }

    /**
     * Draws all "FOOT"-Routes
     */
    private void drawByFoot() {
        for (Route r : Routes.getByFootRoutes()) {
            addRoute(r, Routes.getByFootColor());
        }
    }

    /**
     * Draws all "BICYCLE"-Routes
     */
    private void drawByBicycle() {
        for (Route r : Routes.getBicycleRoutes()) {
            addRoute(r, Routes.getBicycleColor());
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
        for (Player p : manageGame.game.getPlayers()) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(p.getPosition().getLatLng())
                    .icon(BitmapDescriptorFactory.fromResource(p.getIcon()));
            p.setMarker(mMap.addMarker(markerOptions));
            p.getMarker().setTitle(p.getNickname());
            if (p.getNickname().equals(nickname)) {
                myPlayer = p;
            }
        }
        randomEventsEnabled = manageGame.game.isRandomEventsEnabled();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPlayer.getPosition().getLatLng(), 16f), 3000, null);
    }

    @Override
    public void onStartedDiscovery() {
        Log.e(logTag, "Started discovery in GameMap.");
    }

    @Override
    public void onFailedDiscovery() {
        Log.d(logTag, "Failed discovery in GameMap.");
    }

    @Override
    public void onEndpointFound(Map<String, Endpoint> discoveredEndpoints) {
        Log.d(logTag, "Endpoint found in GameMap");
    }

    @Override
    public void onEndpointLost(Map<String, Endpoint> discoveredEndpoints) {
        Log.d(logTag, "Endpoint lost in GameMap");
    }

    @Override
    public void onStoppedDiscovery() {
        Log.d(logTag, "stopped discovery in GameMap");
    }

    @Override
    public void onConnected(Endpoint endpoint) {
        Log.d(logTag, "on connected to endpoint in GameMap");
    }

    @Override
    public void onStartedAdvertising() {
        Log.d(logTag, "Started advertising in GameMap");
    }

    @Override
    public void onFailedAdvertising() {
        Log.d(logTag, "Failed advertising in GameMap");
    }

    @Override
    public void onStoppedAdvertising() {
        Log.d(logTag, "Stopped advertising in GameMap");
    }

    @Override
    public void onConnectionRequested(Endpoint endpoint) {
        Log.d(logTag, "On connection request in GameMap");
    }

    //@Stefan: if activity not running just game must me saved
    @Override
    public void onGameData(Game game) {
        Log.d(logTag, "Got game data");
        if (!isServer) {
            manageGame.game = game;
            if(mMap != null) {
                setupGame();
            }
        }
    }

    //@Stefan: Code should be run even if activity is not running
    @Override
    public void onMessage(Message message) {
        if(!isServer){
            String [] txt = message.getMessage().split(" ");

            if(txt[0].equals("NEXT_ROUND")){
                manageGame.game.nextRound();
                myPlayer.setMoved(false);
                Toast.makeText(GameMap.this, "Runde " + manageGame.game.getRound(), Snackbar.LENGTH_LONG).show();
            }
            if (txt.length == 3 && txt[0].equals("PLAYER") && txt[2].equals("QUITTED")){
                Player player = manageGame.findPlayer(txt[1]);
                deactivatePlayer(player);
            }
            if (txt.length == 2 && txt[0].equals("END")) {
                Toast.makeText(GameMap.this, txt[1] + " hat gewonnen", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void deactivatePlayer(Player player) {
        manageGame.deactivatePlayer(player);
        player.getMarker().remove();
    }

    //@Stefan: Code should be run even if acitivity is not running (just without last line move Marker
    @Override
    public void onSendMove(Move move) {
        Player player = manageGame.findPlayer(move.getNickname());
        int field = move.getField();
        Point point = Points.getPoints()[field];
        Log.d("SEND_MOVE", "receiving move from " + player.getNickname());

        if (isServer) {
            if (player.isMoved()) {
                return;
            }
            serverService.send(move);
            player.setMoved(true);
            tryNextRound();
        }else{
            player.setMoved(true);
        }
        moveMarker(point, player, player.getIcon());
    }

    @Override
    public void onRoadMapEntry(Entry entry) {
        if (isServer) {
            if (!myPlayer.isMrX()) {
                roadMap.addEntry(entry);
                serverService.send(entry);
            }
        } else {
            roadMap.addEntry(entry);
        }
    }

    private void tryNextRound(){
        if(manageGame.tryNextRound() == 1){
            serverService.send(new Message("NEXT_ROUND"));
            Toast.makeText(GameMap.this, "Runde " + manageGame.game.getRound(), Snackbar.LENGTH_LONG).show();
        }else if(manageGame.tryNextRound() == 0){
            serverService.send(new Message("END MisterX")); //MisterX hat gewonnen
            Toast.makeText(GameMap.this, "MisterX hat gewonnen", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailedConnecting(Endpoint endpoint) {
        Log.d(logTag, "Connecting failed in GameMap");
    }

    //@Stefan: Code should be run even if activity is not running
    @Override
    public void onDisconnected(Endpoint endpoint) {
        Log.d(logTag, endpoint.getName() + " has disconnected");
        if (isServer) {
            Player lostPlayer = manageGame.findPlayer(endpoint.getName());
            deactivatePlayer(lostPlayer);
            serverService.send(new Message("PLAYER " + lostPlayer.getNickname() + " QUITTED"));
        } else {
            //TODO: Server lost!
        }
    }

    @Override
    public void onFailedAcceptConnection(Endpoint endpoint) {
        Log.d(logTag, "Failed accepting connection in GameMap");
    }

    @Override
    public void onSendingFailed(Object object) {
        Log.e(logTag, "Sending failed");
        //TODO
    }

    @Override
    public void updateMove(Move move) {
        //TODO
    }

    @Override
    public void showDisconnected(Endpoint endpoint) {
        //TODO
    }

    @Override
    public void showSendingFailed(Object object) {
        //TODO
    }
}