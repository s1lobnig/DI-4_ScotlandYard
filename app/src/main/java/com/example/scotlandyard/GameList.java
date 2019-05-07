package com.example.scotlandyard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.scotlandyard.connection.ClientInterface;
import com.example.scotlandyard.connection.ClientService;
import com.example.scotlandyard.connection.Endpoint;
import com.google.android.gms.nearby.Nearby;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameList extends AppCompatActivity implements ClientInterface {
    private static ListView gameListView; /* ListView GUI Element */
    private MyListAdapter listAdapter; /* Adapter between ListView and  ArrayList<Game> */
    private ClientService clientService; /* ClientService - used for communication with server(s). */
    private ArrayList<Endpoint> endpoints = new ArrayList<>(); /* List of detected endpoints (servers). */
    private Game gameData;
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        clientService.stopDiscovery();
        Log.d(logTag, "Discovery stopped successfully. Initiator: onDestroy()");

        ((ProgressBar) findViewById(R.id.progressBarDiscovery)).setVisibility(View.GONE);
        ((Button) findViewById(R.id.rediscoverButton)).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gameListView = findViewById(R.id.list_currentGames);

        //setAdapter to listView to show all existing games
        listAdapter = new MyListAdapter(this, R.layout.game_item, endpoints);
        gameListView.setAdapter(listAdapter);

        /* Get intent data. */
        Intent intent = getIntent();
        userName = intent.getStringExtra("USERNAME");
        client = new Player(userName);
        client.setHost(false);

        /* Create new client service and start discovery. */
        clientService = ClientService.getInstance(this, userName, Nearby.getConnectionsClient(this));
        clientService.startDiscovery();

        findViewById(R.id.rediscoverButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Check if this is actually working.
                clientService.startDiscovery();
                ((ProgressBar) findViewById(R.id.progressBarDiscovery)).setVisibility(View.VISIBLE);
                ((Button) findViewById(R.id.rediscoverButton)).setVisibility(View.GONE);
            }
        });

        findViewById(R.id.progressBarDiscovery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Check if this is actually working.
                clientService.stopDiscovery();
                ((ProgressBar) findViewById(R.id.progressBarDiscovery)).setVisibility(View.GONE);
                ((Button) findViewById(R.id.rediscoverButton)).setVisibility(View.VISIBLE);
            }
        });
    }

    //need adapter to design a list item and add it to list
    private class MyListAdapter extends ArrayAdapter<Endpoint> {
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
                        /* Request connection to server. */
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
    }

    //what i need in my view
    public class ViewHolder {
        TextView gameName;
        Button playGame;
    }

    @Override
    public void onStartedDiscovery() {
        Log.d(logTag, "Started discovery.");
    }

    @Override
    public void onFailedDiscovery() {
        //TODO display user, that discovery has failed
        Log.d(logTag, "Failed discovery.");
    }

    @Override
    public void onEndpointFound(Map<String, Endpoint> discoveredEndpoints) {
        Log.d(logTag, "A new endpoint has been discovered!");

        /* Get list of all connected endpoints and ignore those which were already there - detect the newly found endpoint. */
        endpoints = new ArrayList<>(discoveredEndpoints.values());
        Log.d(logTag, discoveredEndpoints.values().toString());
        listAdapter.clear();
        listAdapter.addAll(endpoints);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEndpointLost(Map<String, Endpoint> discoveredEndpoints) {
        Log.d(logTag, "Connection with an already discovered endpoint has been lost.");

        this.endpoints = new ArrayList<>(discoveredEndpoints.values());
        //TODO update listAdapter
    }

    @Override
    public void onStoppedDiscovery() {
        //TODO show user, that discovery has stopped
        Log.d(logTag, "Stopped discovery.");

        ((ProgressBar) findViewById(R.id.progressBarDiscovery)).setVisibility(View.GONE);
        ((Button) findViewById(R.id.rediscoverButton)).setVisibility(View.VISIBLE);
    }

    @Override
    public void onConnected(Endpoint endpoint) {
        Log.d(logTag, "Connected with " + endpoint.toString() + " successfully.");

        //TODO show player, that we are connected and waiting for joining the lobby (actually we are waiting for the game object)
    }

    @Override
    public void onGameData(Game game) {
        Log.d(logTag, "Game data received!");
        gameData = game;

        //TODO this should be done on a new Activity, where the lobby (like GameActivity for Server) is shown
        Log.d(logTag, "Loading game map.");
        Intent intent = new Intent(GameList.this, GameMap.class);

        intent.putExtra("CLIENT", client);
        intent.putExtra("USER_NAME", userName);
        intent.putExtra("IS_SERVER", false);
        startActivity(intent);

    }

    @Override
    public void onMessage(Message message) {
        //TODO this should be handled by the messenger in the lobby, if there is one
        Log.d(logTag, "Message data received!");
    }

    @Override
    public void onSendMove(SendMove sendMove) {
        Log.d(logTag, "Move received");
    }

    @Override
    public void onFailedConnecting(Endpoint endpoint) {
        //TODO show user, that connecting has failed
        Log.d(logTag, "Connection to " + endpoint.getName() + " failed!");
    }

    @Override
    public void onDisconnected(Endpoint endpoint) {
        Log.d(logTag, "Endpoint " + endpoint.getName() + " disconnected successfully!");
    }

    @Override
    public void onFailedAcceptConnection(Endpoint endpoint) {
        //TODO show user, that accepting the connection has failed
        Log.d(logTag, "Endpoint " + endpoint.getName() + " has failed to accept connection!");
    }

    @Override
    public void onSendingFailed(Object object) {
        //TODO show user, that sending has failed
        Log.d(logTag, "Failed to send the message!");
    }
}