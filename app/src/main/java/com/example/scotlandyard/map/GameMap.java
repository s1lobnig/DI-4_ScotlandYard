package com.example.scotlandyard.map;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.example.scotlandyard.gameend.GameEndActivity;
import com.example.scotlandyard.MainActivity;
import com.example.scotlandyard.QuitNotification;
import com.example.scotlandyard.control.Server;
import com.example.scotlandyard.control.Device;
import com.example.scotlandyard.control.GameInterface;
import com.example.scotlandyard.map.motions.LatLngInterpolator;
import com.example.scotlandyard.map.motions.MarkerAnimation;
import com.example.scotlandyard.map.motions.MarkerMovingRoute;
import com.example.scotlandyard.map.motions.MovingLogic;
import com.example.scotlandyard.map.motions.RandomEvent;
import com.example.scotlandyard.map.motions.Move;
import com.example.scotlandyard.map.roadmap.Entry;
import com.example.scotlandyard.map.roadmap.RoadMapDialog;
import com.example.scotlandyard.messenger.Messenger;
import com.example.scotlandyard.Player;
import com.example.scotlandyard.PlayersOverview;
import com.example.scotlandyard.R;
import com.example.scotlandyard.Settings;
import com.example.scotlandyard.connection.Endpoint;
import com.example.scotlandyard.Game;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.SecureRandom;
import java.util.Random;

import static com.example.scotlandyard.R.color.colorPrimary;
import static com.example.scotlandyard.R.color.colorPrimaryDark;

public class GameMap extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GameInterface {

    private static Device device;
    private static final Random RANDOM = new SecureRandom();

    private TextView rounds;
    private TextView pedestrianTickets;
    private TextView bicycleTickets;
    private TextView busTickets;
    private TextView taxiTickets;
    private TextView blackTickets;
    private TextView doubleTickets;
    private TextView playerName;
    private ImageView playerImage;

    private static final String TAG = GameMap.class.getSimpleName();
    private GoogleMap mMap;
    private static Player myPlayer;
    private boolean randomEventsEnabled;

    private static final int BY_FOOT_COLOR = Color.YELLOW;
    private static final int BUS_COLOR = Color.RED;
    private static final int BICYCLE_COLOR = Color.rgb(255, 164, 17);
    private static final int TAXI_DRAGAN_COLOR = Color.BLUE;


    private SensorManager sm;

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
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(device.getNickname());
        setSupportActionBar(toolbar);

        rounds = findViewById(R.id.round);

        pedestrianTickets = findViewById(R.id.pedestrianTicket);
        bicycleTickets = findViewById(R.id.bicycleTicket);
        busTickets = findViewById(R.id.busTicket);
        blackTickets = findViewById(R.id.blackTicket);
        blackTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(GameMap.this);
                dialog.setContentView(R.layout.black_ticket_dialog);
                Button useTicket = (Button) dialog.findViewById(R.id.btnUseTicket);
                Button cancel = (Button) dialog.findViewById(R.id.btnCancel);

                // if button is clicked, close the custom dialog
                useTicket.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (myPlayer.getTickets().get(R.string.BLACK_TICKET_KEY).intValue() == 0) {
                            Toast.makeText(GameMap.this, R.string.notEnoughTickets, Snackbar.LENGTH_LONG).show();
                        } else {
                            myPlayer.setSpecialMrXMoves(true, 0);
                        }
                        dialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });
        taxiTickets = findViewById(R.id.taxiTicket);
        doubleTickets = findViewById(R.id.doubleTicket);
        doubleTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(GameMap.this);
                dialog.setContentView(R.layout.double_ticket_dialog);
                Button useTicket = (Button) dialog.findViewById(R.id.btnUseTicket);
                Button cancel = (Button) dialog.findViewById(R.id.btnCancel);

                // if button is clicked, close the custom dialog
                useTicket.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (myPlayer.getTickets().get(R.string.DOUBLE_TICKET_KEY).intValue() == 0) {
                            Toast.makeText(GameMap.this, R.string.notEnoughTickets, Snackbar.LENGTH_LONG).show();
                        } else {
                            myPlayer.setSpecialMrXMoves(true, 1);
                        }
                        dialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(GameMap.this, Messenger.class);
                mIntent.putExtra("USERNAME", device.getNickname());
                startActivity(mIntent);

            }
        });
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(colorPrimaryDark)));

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        this.playerImage = headerView.findViewById(R.id.player_image);
        this.playerName = headerView.findViewById(R.id.player_name);
        Log.d(TAG, playerImage.toString());
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

        SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(sensorListenerProximity, sm.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onStop() {
        super.onStop();

        Device.getInstance().removeGameObserver();
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
        } else if (id == R.id.nav_road_map) {
            DialogFragment roadMapDialog = new RoadMapDialog();
            Bundle args = new Bundle();
            args.putSerializable("ROAD_MAP", device.getRoadMap());
            roadMapDialog.setArguments(args);
            roadMapDialog.show(getSupportFragmentManager(), "RoadMapDisplay");
        }else if(id == R.id.nav_logout){
            if(!device.isServer()){
                device.send(new QuitNotification(device.getNickname(), false));
                intent = new Intent(this, MainActivity.class);
            }else{
                device.send(new QuitNotification(device.getNickname(), true));
                intent = new Intent(this, MainActivity.class);
            }

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
            device.getGame().givePlayerPositionAndIcon();
            device.sendGame();
            setupGame();
            visualizeTickets();
            if (device.getGame().isBotMrX()) {
                ((Server) device).moveBot();
            }

        } else {
            setupGame();
            visualizeTickets();
            randomEventsEnabled = device.getGame().isRandomEventsEnabled();
        }
        playerName.setText(myPlayer.getNickname());
        playerImage.setImageResource(myPlayer.getIcon());
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker field) {
                Point newLocation = new Point(field.getPosition().latitude, field.getPosition().longitude);
                if (!Device.getInstance().getGame().isPlayer(field) && isValidMove(newLocation)) {
                    int r = RANDOM.nextInt(100) % 10;
                    int idx = Points.getIndex(newLocation);
                    ValidatedRoute randomRoute = Routes.getRandomRoute(Points.getIndex(myPlayer.getPosition()) + 1, idx + 1);
                    Move move = new Move(myPlayer.getNickname(), Points.getIndex(newLocation), r, randomRoute);

                    if (Device.isServer()) {
                        if (myPlayer.getCountCheatingMoves() > 0)
                            move.setCheatingMove(true);

                        ((Server) device).onDataReceived(move, myPlayer.getNickname());
                    } else {
                        if (myPlayer.getCountCheatingMoves() > 0) {
                            move.setCheatingMove(true);
                            myPlayer.decCountCheatingMoves();
                        }

                        device.send(move);
                    }
                    return true;
                }
                return false;
            }
        });
    }


    private boolean isValidMove(Point newLocation) {
        switch (myPlayer.isValidMove(Device.getInstance().getGame(), newLocation)) {
            case 0:
                return true;
            case 2:
                // Toast to indicate that it is not your turn
                Toast.makeText(GameMap.this, "Du kannst nicht mehr ziehen und bist daher deaktiviert.", Snackbar.LENGTH_LONG).show();
                return false;
            case 3:
                Toast.makeText(GameMap.this, "Ein anderer Spieler ist noch nicht gezogen. Du musst noch warten.", Snackbar.LENGTH_LONG).show();
                return false;
            case 4:
                // Toast to indicate that the clicked location is not reachable from the current
                // location
                Toast.makeText(GameMap.this, "Feld nicht erreichbar", Snackbar.LENGTH_LONG).show();
                return false;
            case 5:
                Toast.makeText(GameMap.this, "Das Fahrrad ist noch nicht verfügbar!", Snackbar.LENGTH_LONG).show();
                return false;
            case 6:
                //Toast to indicate that player has not enough tickets for reachable field
                Toast.makeText(GameMap.this, R.string.notEnoughTickets, Snackbar.LENGTH_LONG).show();
                return false;
            case 7:
                Toast.makeText(GameMap.this, "KEINE TICKETS MEHR. Du wurdest deaktiviert", Snackbar.LENGTH_LONG).show();
                if (!Device.isServer()) {
                    device.send(new MapNotification(myPlayer.getNickname() + " DEACTIVATED"));
                }
                return false;
            case 8:
                Toast.makeText(GameMap.this, "KEIN ZUG MEHR MÖGLICH. Du wurdest deaktiviert.", Snackbar.LENGTH_LONG).show();
                if (!Device.isServer()) {
                    device.send(new MapNotification(myPlayer.getNickname() + " DEACTIVATED"));
                }
                return false;
            default:
                return false;
        }
    }

    private boolean moveMarker(Point p, Player player, int playerIcon, int r, ValidatedRoute randomRoute) {
        if (randomEventsEnabled && r <= 3 && player.getPenalty() == 0) {
            return moveWithRandomEvent(player, p, playerIcon, r, randomRoute);
        }
        return move(player, p, false, false, playerIcon, randomRoute);
    }

    public boolean moveWithRandomEvent(Player player, Point p, int playerIcon, int randomEvent, ValidatedRoute route) {
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
            return move(player, p, goBack, randomRoute, playerIcon, route);
        }
        visualizeTickets();
        return false;
    }

    public boolean move(Player player, Point p, boolean goBack, boolean randomRoute, int playerIcon, ValidatedRoute randRoute) {
        MarkerMovingRoute markerMove = MovingLogic.prepareMove(player, randomRoute, randRoute, p);
        visualizeTickets();

        if (player.isMrX() && ((player.equals(myPlayer) || (device.getGame().isBotMrX() && Device.isServer())))) {
            int lastTurn = device.getRoadMap().getNumberOfEntries();
            Entry entry = MovingLogic.getRoadMapEntry(lastTurn, markerMove.getNewLocation(), markerMove.getTicket());
            if (Device.isServer()) {
                device.getRoadMap().addEntry(entry);
            }
            device.send(entry);
        }

        player.getMarker().setIcon(BitmapDescriptorFactory.fromResource(markerMove.getIcon()));
        markerMove = MovingLogic.createMove(markerMove, goBack, player);
        runMarkerAnimation(player, markerMove, playerIcon);
        return true;
    }


    public static void runMarkerAnimation(Player player, MarkerMovingRoute movingRoute, int finalIcon) {
        if (movingRoute.getIntermediates() == null || movingRoute.getIntermediates().isEmpty()) {
            MarkerAnimation.moveMarkerToTarget(player.getMarker(), movingRoute.getFinalPosition(), new LatLngInterpolator.Linear(), MovingLogic.ANIMATION_DURATION, movingRoute.getIcon(), finalIcon);
        } else {
            MarkerAnimation.moveMarkerToTarget(player.getMarker(), movingRoute.getIntermediates(), movingRoute.getTimeSlices(), movingRoute.getFinalPosition(), new LatLngInterpolator.Linear(), movingRoute.getIcon(), finalIcon);
        }
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
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(p_LatLng)
                    .icon(BitmapDescriptorFactory.fromResource(p.getIcon()));
            mMap.addMarker(markerOptions.anchor(0.5f, 0.5f));
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
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(p.getPosition().getLatLng())
                    .icon(BitmapDescriptorFactory.fromResource(p.getIcon()));
            p.setMarker(mMap.addMarker(markerOptions));
            p.getMarker().setTitle(p.getNickname());
            if (p.getNickname().equals(device.getNickname())) {
                myPlayer = p;
            }
        }
        randomEventsEnabled = device.getGame().isRandomEventsEnabled();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPlayer.getPosition().getLatLng(), 16f), 3000, null);
    }

    @Override
    public void updateMove(Move move) {
        Player player = device.getGame().findPlayer(move.getNickname());
        int field = move.getField();
        Point point = Points.getPoints()[field];

        if (myPlayer.getNickname() != player.getNickname() && move.isCheatingMove()) {
            Log.d(TAG,"MR X schummelt");
        }

        moveMarker(point, player, player.getIcon(), move.getRandomEventTrigger(), move.getRandomRoute());
    }

    @Override
    public void showDisconnected(Endpoint endpoint) {
        if (Device.isServer()) {
            Toast.makeText(GameMap.this, "Verbindung zu Player " + endpoint.getName() + " verloren!", Toast.LENGTH_LONG).show();
            //TODO: Server lost!
        } else {
            Toast.makeText(GameMap.this, "Verbindung zu Server verloren!", Toast.LENGTH_LONG).show();
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
        Log.d(TAG,notification + " konnte nicht gesendet werden!");

    }

    @Override
    public void onMessage() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(colorPrimary)));
    }

    @Override
    public void onReceivedToast(String toast) {
        if (toast.contains("Runde")) {
            rounds.setText(toast);
        } else if (toast.contains("gewonnen")) {
            rounds.setText(toast);
        } else {
            Toast.makeText(GameMap.this, toast, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showReconnected(String endpointName) {
        Toast.makeText(GameMap.this, "Verbindung zu " + endpointName + " wiederhergestellt!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showReconnectFailed(String endpointName) {
        Toast.makeText(GameMap.this, "Verbindung zu " + endpointName + " konnte nicht wiederhergestellt werden!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showNewGame(Game game) {
        if (mMap != null) {
            deleteMarker();
            Device.getInstance().setGame(game);
            setupGame();
        }
    }


    @Override
    public void onQuit(String playerName, boolean serverQuit) {
        if (serverQuit) {
            //start new intent main activity, server has quited
            Toast.makeText(this, playerName + " hat das Spiel beendet.", Snackbar.LENGTH_LONG).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            //show that playerName has quited
            Toast.makeText(this, playerName + " hat das Spiel verlassen.", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        try {
            Device.getInstance().addGameObserver(this);
        } catch (IllegalStateException ex) {
            Log.d("GameMap", "gameObserver already added");
        }
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(colorPrimaryDark)));
        if (mMap != null) {
            deleteMarker();
            setupGame();
        }
    }

    private void deleteMarker() {
        for (Player player : Device.getInstance().getGame().getPlayers()) {
            player.getMarker().remove();
        }
    }

    //If proximitry listener is activated, this methode is called
    private final SensorEventListener sensorListenerProximity = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float distance = event.values[0];
            if (distance < 5 && myPlayer.isMrX()) {
                Toast.makeText(GameMap.this, "Weitere Bewegung ausführen", Snackbar.LENGTH_LONG).show();
                myPlayer.setHasCheated(true);
                myPlayer.setHasCheatedThisRound(true);
                if (myPlayer.getCountCheatingMoves() == 0) {
                    myPlayer.incCountCheatingMoves();
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (device.getGame().isBotMrX()) {
            Player bot = device.getGame().findPlayer("Bot");
            Device.getInstance().getLobby().getPlayerList().remove(bot);
        }
        for (Player player : device.getGame().getPlayers()) {
            player.resetPlayer();
        }
        device = null;
        myPlayer = null;
    }

    @Override
    public void onRecievedEndOfGame(boolean hasMrXWon) {
        Intent i = new Intent(GameMap.this, GameEndActivity.class);
        i.putExtra("Winner", hasMrXWon);
        startActivity(i);
    }
}
