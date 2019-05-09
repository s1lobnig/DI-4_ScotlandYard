package com.example.scotlandyard;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.example.scotlandyard.connection.ClientInterface;
import com.example.scotlandyard.connection.ClientService;
import com.example.scotlandyard.connection.Endpoint;
import com.example.scotlandyard.connection.ServerInterface;
import com.example.scotlandyard.connection.ServerService;
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
import java.util.Map;
import java.util.Random;

public class GameMap extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, ClientInterface, ServerInterface {

    private static ServerService serverService;
    private static ClientService clientService;
    private static Random randomNumber = new Random();

    private static boolean isServer;
    private static String logTag;
    private static String nickname;
    private static final String TAG = GameMap.class.getSimpleName();
    private GoogleMap mMap;
    private static int playerPenaltay = 0;
    private static int round = 1;
    private static Game game;
    private static Player myPlayer;
    private static final int[] PLAYER_ICONS = {
            R.drawable.player1,
            R.drawable.player2,
            R.drawable.player3,
            R.drawable.player4,
            R.drawable.player5,
            R.drawable.player6,
            R.drawable.player7,
            R.drawable.player8,
            R.drawable.player9,
            R.drawable.player10,
            R.drawable.player11
    };

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_navigation);

        //if game has not started yet
        if(game == null) {
            Intent intent = getIntent();
            nickname = intent.getStringExtra("USERNAME");
            isServer = intent.getBooleanExtra("IS_SERVER", true);

            if (isServer) {
                game = ((Game) intent.getSerializableExtra("GAME"));
                serverService = ServerService.getInstance();
                serverService.setServer(this);
                logTag = "SERVER_SERVICE";
            } else {
                clientService = ClientService.getInstance();
                clientService.setClient(this);
                logTag = "CLIENT_SERVICE";
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(nickname);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(GameMap.this, Messanger.class);
                mIntent.putExtra("USERNAME", nickname);
                mIntent.putExtra("IS_SERVER", isServer);
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

        //if game has not startet yet
        if(myPlayer == null) {
            if(isServer) {
                Player player;
                for (int i = 0; i < game.getPlayers().size(); i++) {
                    player = game.getPlayers().get(i);
                    player.setIcon(PLAYER_ICONS[i]);
                    player.setMarker(initializeMarker(PLAYER_ICONS[i]));
                    player.getMarker().setTitle(player.getNickname());
                    LatLng position = player.getMarker().getPosition();
                    player.setPosition(new Point(position.latitude, position.longitude));
                    player.setMoved(false);
                }
                myPlayer = findPlayer(nickname);
                serverService.send(game);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPlayer.getPosition().getLatLng(), 16f));
            }
        }else{
            setupGame();
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker field) {
                if(!isPlayer(field)) {
                    if (!myPlayer.isMoved()) {
                        boolean isValid = isValidMove(field, myPlayer.getMarker());
                        if (isValid) {
                            if (isServer) {
                                Point point = Points.getPoints()[getFeeldnumber(field)];
                                moveMarker(point, myPlayer.getMarker(), myPlayer.getIcon());
                                serverService.send(new SendMove(myPlayer.getNickname(), getFeeldnumber(field)));
                                myPlayer.setMoved(true);
                                tryNextRound();
                            } else {
                                clientService.send(new SendMove(myPlayer.getNickname(), getFeeldnumber(field)));
                            }
                        } else {
                            // Toast to indicate that the clicked location is not reachable from the current
                            // location
                            Toast.makeText(GameMap.this, "Feld nicht erreichbar", Snackbar.LENGTH_LONG).show();
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

    private boolean isPlayer(Marker field) {
        for (Player player : game.getPlayers()) {
            if(player.getMarker().equals(field)){
                return true;
            }
        }
        return false;
    }

    private boolean movewithrandomEvent(Marker player, Point p, int playerIcon) {
        RandomEvent r = new RandomEvent();
        boolean goback = false;
        boolean dontgo = false;
        boolean randomRoute = false;

        if(r.getID() == 0){
            Toast.makeText(GameMap.this, r.getText(), Snackbar.LENGTH_LONG).show();
            dontgo = true;
        }
        else if(r.getID() == 1){
            Toast.makeText(GameMap.this, r.getText(), Snackbar.LENGTH_LONG).show();
            goback = true;
        }
        else if(r.getID() == 2 ){
            Toast.makeText(GameMap.this, r.getText(), Snackbar.LENGTH_LONG).show();
            playerPenaltay = 3;
        }
        else if (r.getID() == 3){
            Toast.makeText(GameMap.this, r.getText(), Snackbar.LENGTH_LONG).show();
            randomRoute = true;
        }
        if(!dontgo){
            return move(player,p,goback, randomRoute, playerIcon);
        }
        return false;
    }

    private boolean moveMarker(Point p, Marker player, int playerIcon) {
        int r = randomNumber.nextInt(100)%10;
        //System.out.println("###################"+r+"-"+playerPenaltay);
        if(false && r < 3) {
            if (playerPenaltay == 0){
                System.out.println("*********************RANDOM EVENT HAPPENING");
                return movewithrandomEvent(player, p,playerIcon);
            }

        }
        return move(player, p, false, false, playerIcon);
    }

    private boolean isValidMove(Marker destination, Marker player){
        LatLng current = player.getPosition();
        Point currentPoint = new Point(current.latitude, current.longitude);
        Point newLocation = new Point(destination.getPosition().latitude, destination.getPosition().longitude);
        Object[] routeToTake = Routes.getRoute(Points.getIndex(currentPoint), Points.getIndex(newLocation));
        return (Boolean) routeToTake[0];
    }

    private boolean move(Marker player, Point p, boolean goBack, boolean randomRoute, int playerIcon){
        LatLng current = player.getPosition();
        Point currentPoint = new Point(current.latitude, current.longitude);
        Point newLocation = p;
        Object[] routeToTake = Routes.getRoute(Points.getIndex(currentPoint), Points.getIndex(newLocation));
        boolean isValid = (Boolean) routeToTake[0];
        // if the route would be valid but there is the randowm event "verfahren", then...
        // Note, this does not work at the moment!
        if(isValid && randomRoute){
            routeToTake = Routes.getRandomRoute(Points.getIndex(currentPoint), Points.getIndex(newLocation));
        }
        if (isValid) {
            Route r = (Route) routeToTake[1];
            int icon;
            int vehicle = (int) routeToTake[2];
            switch (vehicle) {
                case 0:
                    icon = R.drawable.pedestrian;
                    break;
                case 1:
                    icon = R.drawable.bicycle;
                    break;
                case 2:
                    icon = R.drawable.bus;
                    break;
                case 3:
                    icon = R.drawable.taxi;
                    break;
                default:
                    icon = -1;
            }
            int animationDuration = 3000;
            if(!(playerPenaltay > 0 && icon == R.drawable.bicycle)) {
                if(playerPenaltay > 0)
                    playerPenaltay--;
                if (r.getIntermediates() != null) {
                    player.setIcon(BitmapDescriptorFactory.fromResource(icon));
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
                        finalPos = player.getPosition();
                    }
                    MarkerAnimation.moveMarkerToTarget(player, routePoints, timeSlices, finalPos, new LatLngInterpolator.Linear(), icon, false, GameMap.this,playerIcon);
                } else {
                    if (!goBack) {
                        MarkerAnimation.moveMarkerToTarget(player, p.getLatLng(), new LatLngInterpolator.Linear(), animationDuration, icon,playerIcon);
                    } else {
                        // if rand event, then...
                        ArrayList<Float> timeSlices = new ArrayList<>();
                        timeSlices.add((float) animationDuration);
                        timeSlices.add((float) animationDuration);
                        ArrayList<LatLng> routePoints = new ArrayList<>();
                        routePoints.add(p.getLatLng());
                        MarkerAnimation.moveMarkerToTarget(player, routePoints, timeSlices, player.getPosition(), new LatLngInterpolator.Linear(), icon, true, GameMap.this,playerIcon);
                    }
                }
            }
            else{
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
     *         current position = route.StartPos) or revered) ArrayList<Float> which
     *         contains the animation-duration-slices according to the
     *         route-part-length }
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
        return new Object[] { routePoints, timeSlices };
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
        return new Object[] { routePoints, timeSlices };
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

    private int getFeeldnumber(Marker feeld){
        Point newLocation = new Point(feeld.getPosition().latitude, feeld.getPosition().longitude);
        return Points.getIndex(newLocation);
    }

    /**
     * Initializes the player-marker and sets its icon to param icon
     *
     * @param icon player-icon
     * @return the resulting player-marker
     */
    private Marker initializeMarker(int icon) {
        int position = (randomNumber).nextInt(Points.getPoints().length);
        LatLng latLng = Points.POINTS[position].getLatLng();
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng));
        marker.setIcon(BitmapDescriptorFactory.fromResource(icon));
        return marker;
    }

    private void setupGame(){
        for (Player p : game.getPlayers()) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(p.getPosition().getLatLng())
                    .icon(BitmapDescriptorFactory.fromResource(p.getIcon()));
            p.setMarker(mMap.addMarker(markerOptions));
            if(p.getNickname().equals(nickname)){
                myPlayer = p;
            }
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPlayer.getPosition().getLatLng(), 16f));
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

    @Override
    public void onGameData(Game game) {
        Log.d(logTag, "Got game data");
        if(!isServer){
            this.game = (Game) game;
            setupGame();
        }
    }

    @Override
    public void onMessage(Message message) {
        if(!isServer){
            String [] txt = ((Message) message).getMessage().split(" ");

            if(txt[0].equals("NEXT_ROUND")){
                round++;
                myPlayer.setMoved(false);
                Toast.makeText(GameMap.this, "Runde " + round, Snackbar.LENGTH_LONG).show();
            }
            if(txt.length == 3 && txt[0].equals("PLAYER") && txt[2].equals("QUITTED")){
                Player player = findPlayer(txt[1]);
                deactivatePlayer(player);
            }
            if(txt.length == 2 && txt[0].equals("END")){
                Toast.makeText(GameMap.this, txt[1] + " hat gewonnen", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void deactivatePlayer(Player player) {
        player.setMoved(true); //so he does not have to move in this round
        player.setActive(false);
        player.getMarker().remove();
    }

    @Override
    public void onSendMove(SendMove sendMove) {
        Player player = findPlayer(((SendMove)sendMove).getNickname());
        //if it is my Player to move
        if(player.getNickname().equals(myPlayer.getNickname())){
            myPlayer.setMoved(true);
        }
        int field = ((SendMove)sendMove).getField();
        Point point = Points.getPoints()[field];

        Log.d("SEND_MOVE","receiving move from " + player.getNickname());

        if(isServer){
            if(player.isMoved()) {
                return;
            }
            player.setMoved(true);
            serverService.send(sendMove);
            tryNextRound();
        }
        moveMarker(point, player.getMarker(), player.getIcon());
    }

    private void tryNextRound() {
        if(isRoundFinished()){
            if(round < 12) {
                round++;
                for (Player p : game.getPlayers()) {
                    p.setMoved(false);
                }
                serverService.send(new Message("NEXT_ROUND"));
                Toast.makeText(GameMap.this, "Runde " + round, Snackbar.LENGTH_LONG).show();
            }else{
                serverService.send(new Message("END MisterX")); //MisterX hat gewonnen
                Toast.makeText(GameMap.this, "MisterX hat gewonnen", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private boolean isRoundFinished() {
        for (Player p : game.getPlayers()) {
            if(p.isActive() && !p.isMoved()){
                return false;
            }
        }
        return true;
    }

    private Player findPlayer(String nickname) {
        for (Player p : game.getPlayers()) {
            if(p.getNickname().equals(nickname)){
                return p;
            }
        }
        return null;
    }

    @Override
    public void onFailedConnecting(Endpoint endpoint) {
        Log.d(logTag, "Connecting failed in GameMap");
    }

    @Override
    public void onDisconnected(Endpoint endpoint) {
        Log.d(logTag, endpoint.getName() + " has disconnected");
        if(isServer) {
            Player lostPlayer = findPlayer(endpoint.getName());
            deactivatePlayer(lostPlayer);
            serverService.send(new Message("PLAYER " + lostPlayer.getNickname() + " QUITTED"));
        }else{
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
}
