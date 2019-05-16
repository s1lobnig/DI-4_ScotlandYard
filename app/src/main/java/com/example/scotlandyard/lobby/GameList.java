package com.example.scotlandyard.lobby;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scotlandyard.map.roadmap.Entry;
import com.example.scotlandyard.messenger.Message;
import com.example.scotlandyard.Player;
import com.example.scotlandyard.R;
import com.example.scotlandyard.map.motions.SendMove;
import com.example.scotlandyard.connection.ClientInterface;
import com.example.scotlandyard.connection.ClientService;
import com.example.scotlandyard.connection.Endpoint;
import com.google.android.gms.nearby.Nearby;

import java.util.ArrayList;
import java.util.Map;

public class GameList extends AppCompatActivity implements ClientInterface {

    private static ListView gameListView; /* ListView GUI Element */
    private ArrayAdapter listAdapter; /* Adapter between ListView and  ArrayList<Game> */

    private ClientService clientService; /* ClientService - used for communication with server(s). */
    private ArrayList<Endpoint> endpoints = new ArrayList<>(); /* List of detected endpoints (servers). */
    private String userName;
    private Player client;
    private String logTag = "CLIENT_SERVICE";

    @Override
    protected void onStop() {
        super.onStop();

        clientService.stopDiscovery();
        Log.d(logTag, "Discovery stopped successfully. Initiator: stopDiscovery()");

        ((ProgressBar) findViewById(R.id.progressBarDiscovery)).setVisibility(View.GONE);
        ((Button) findViewById(R.id.rediscoverButton)).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.textInfo)).setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        clientService.stopDiscovery();
        Log.d(logTag, "Discovery stopped successfully. Initiator: onDestroy()");

        ((ProgressBar) findViewById(R.id.progressBarDiscovery)).setVisibility(View.GONE);
        ((Button) findViewById(R.id.rediscoverButton)).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.textInfo)).setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gameListView = findViewById(R.id.list_currentGames);

        //setAdapter to listView to show all existing games
        listAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, endpoints);
        gameListView.setAdapter(listAdapter);

        /* Get intent data. */
        Intent intent = getIntent();
        userName = intent.getStringExtra("USERNAME");
        client = new Player(userName);
        client.setHost(false);

        /* Called when clicked on an item from the list of available servers. */
        gameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /* After the button 'playGame' has been clicked the discovery will stop. */
                clientService.stopDiscovery();

                /* Start Client Lobby activity. */
                Log.d(logTag, "Loading client lobby.");
                Intent intent = new Intent(GameList.this, ClientLobby.class);
                intent.putExtra("USERNAME", GameList.this.client.getNickname());
                intent.putExtra("ENDPOINT_NAME", endpoints.get((int) id).getName());
                intent.putExtra("ENDPOINT_ID", endpoints.get((int) id).getId());
                startActivity(intent);
            }
        });

        if (ClientService.isSingletonSet()) {
            ClientService.resetInstance();
        }
        try {
            clientService = ClientService.setInstance(this, userName, Nearby.getConnectionsClient(this));
        } catch (IllegalStateException ex) {
            Log.d(logTag, "failed setting instance", ex);
        }
        clientService.startDiscovery();

        findViewById(R.id.rediscoverButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Check if this is actually working.
                clientService.startDiscovery();
                ((ProgressBar) findViewById(R.id.progressBarDiscovery)).setVisibility(View.VISIBLE);
                ((Button) findViewById(R.id.rediscoverButton)).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.textInfo)).setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.progressBarDiscovery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Check if this is actually working.
                clientService.stopDiscovery();
                ((ProgressBar) findViewById(R.id.progressBarDiscovery)).setVisibility(View.GONE);
                ((Button) findViewById(R.id.rediscoverButton)).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.textInfo)).setVisibility(View.GONE);
            }
        });
    }

    //need adapter to design a list item and add it to list
    /*private class MyListAdapter extends ArrayAdapter<Endpoint> {
        private int layout;
        private String logTag = "CLIENT_SERVICE";

        public MyListAdapter(Context context, int resource, List<Endpoint> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        //build the design
        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewHolder = new ViewHolder();
            final Endpoint game = getItem(position);


            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);

                mainViewHolder.gameName = (TextView) convertView.findViewById(R.id.txtgameName);
                mainViewHolder.playGame = (Button) convertView.findViewById(R.id.btnPlayGame);
                mainViewHolder.playGame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Request connection to server.
                        Log.d(logTag, "Requesting connection to "+endpoints.get(position));
                        clientService.connectToEndpoint(endpoints.get(position));

                    }
                });
                convertView.setTag(mainViewHolder);

            }

            //set Text from stored variables to views
            mainViewHolder = (ViewHolder) convertView.getTag();
            mainViewHolder.gameName.setText(game.getName());

            //returns finished view
            return convertView;

        }
    }*/

    //what i need in my view
    public class ViewHolder {
        TextView gameName;
        Button playGame;
    }

    @Override
    public void onStartedDiscovery() {
        Log.d(logTag, "Started discovery.");

        ((ProgressBar) findViewById(R.id.progressBarDiscovery)).setVisibility(View.VISIBLE);
        ((Button) findViewById(R.id.rediscoverButton)).setVisibility(View.GONE);
        ((TextView) findViewById(R.id.textInfo)).setVisibility(View.VISIBLE);
    }

    @Override
    public void onFailedDiscovery() {
        String notification = "Es konnte kein Server gefunden werden (das Gerät unterstützt die Nearby API nicht oder die Ressourcen wurden bereits verwendet).";
        Toast.makeText(getApplicationContext(), notification, Toast.LENGTH_LONG).show();

        Log.d(logTag, "Failed discovery.");
    }

    @Override
    public void onEndpointFound(Map<String, Endpoint> discoveredEndpoints) {
        Log.d(logTag, "A new endpoint has been discovered!");

        endpoints = new ArrayList<>(discoveredEndpoints.values());
        Log.d(logTag, discoveredEndpoints.values().toString());
        listAdapter.clear();
        listAdapter.addAll(endpoints);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEndpointLost(Map<String, Endpoint> discoveredEndpoints) {
        Log.d(logTag, "Connection with an already discovered endpoint has been lost.");

        endpoints = new ArrayList<>(discoveredEndpoints.values());
        Log.d(logTag, discoveredEndpoints.values().toString());
        listAdapter.clear();
        listAdapter.addAll(endpoints);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStoppedDiscovery() {
        Log.d(logTag, "Stopped discovery.");

        ((ProgressBar) findViewById(R.id.progressBarDiscovery)).setVisibility(View.GONE);
        ((Button) findViewById(R.id.rediscoverButton)).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.textInfo)).setVisibility(View.GONE);
    }

    /* Not needed anymore. It will be handled inside ClientLobby. */
    @Override
    public void onConnected(Endpoint endpoint) {
        Log.d(logTag, "Connected with " + endpoint.toString() + " successfully.");

        String notification = "Sie sind verbunden und warten darauf, der Lobby beizutreten.";
        Toast.makeText(getApplicationContext(), notification, Toast.LENGTH_LONG).show();
    }

    /* Not needed anymore. It will be handled inside ClientLobby. */
    @Override
    public void onGameData(Game game) {
        Log.d(logTag, "Game data received!");

        /*
        gameData = game;

        Log.d(logTag, "Loading game map.");
        Intent intent = new Intent(GameList.this, GameMap.class);

        intent.putExtra("CLIENT", client);
        intent.putExtra("USERNAME", userName);
        intent.putExtra("IS_SERVER", false);
        startActivity(intent);
        */
    }

    /* Not needed anymore. It will be handled inside ClientLobby. */
    @Override
    public void onMessage(Message message) {
        Log.d(logTag, "Message data received!");
    }

    /* Not needed anymore. It will be handled inside ClientLobby. */
    @Override
    public void onSendMove(SendMove sendMove) {
        Log.d(logTag, "Move received");
    }

    @Override
    public void onRoadMapEntry(Entry entry) {

    }

    /* Not needed anymore. It will be handled inside ClientLobby. */
    @Override
    public void onFailedConnecting(Endpoint endpoint) {

        Log.d(logTag, "Connection to " + endpoint.getName() + " failed!");
    }

    /* Not needed anymore. It will be handled inside ClientLobby. */
    @Override
    public void onDisconnected(Endpoint endpoint) {
        Log.d(logTag, "Endpoint " + endpoint.getName() + " disconnected successfully!");
    }

    /* Not needed anymore. It will be handled inside ClientLobby. */
    @Override
    public void onFailedAcceptConnection(Endpoint endpoint) {
        Log.d(logTag, "Endpoint " + endpoint.getName() + " has failed to accept connection!");
    }

    /* Not needed anymore. It will be handled inside ClientLobby. */
    @Override
    public void onSendingFailed(Object object) {
        Log.d(logTag, "Failed to send the message!");
    }
}