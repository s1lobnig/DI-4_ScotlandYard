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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameList extends AppCompatActivity implements ClientInterface {
    private static ListView gameListView; /* ListView GUI Element */
    private MyListAdapter listAdapter; /* Adapter between ListView and  ArrayList<Game> */
    private ClientService clientService; /* ClientService - used for communication with server(s). */
    private ArrayList<Endpoint> endpoints = new ArrayList<>(); /* List of detected endpoints (servers). */
    private ArrayList<Game> games = new ArrayList<>(); /* List of available games (or game servers). */
    private Game myData = new Game("", -1);

    @Override
    protected void onStop() {
        super.onStop();

        clientService.stopDiscovery();
        Log.d("SERVER_SERVICE", "Discovery stopped successfully. Initiator: stopDiscovery()");

        ((ProgressBar) findViewById(R.id.progressBarDiscovery)).setVisibility(View.GONE);
        ((Button) findViewById(R.id.rediscoverButton)).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        clientService.stopDiscovery();
        Log.d("SERVER_SERVICE", "Discovery stopped successfully. Initiator: onDestroy()");

        ((ProgressBar) findViewById(R.id.progressBarDiscovery)).setVisibility(View.GONE);
        ((Button) findViewById(R.id.rediscoverButton)).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Create new client service and start discovery. */
        clientService = ClientService.getInstance(this, android.os.Build.MODEL, this);
        clientService.startDiscovery();

        gameListView = findViewById(R.id.list_currentGames);

        //setAdapter to listView to show all existing games
        gameListView.setAdapter(listAdapter = new MyListAdapter(this, R.layout.game_item, games));

        /* Get intent data. */
        Intent intent = getIntent();
        String username = intent.getStringExtra("USERNAME");
        myData.getPlayers().add(new Player(username));

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
    private class MyListAdapter extends ArrayAdapter<Game> {
        private int layout;

        public MyListAdapter(Context context, int resource, List<Game> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        //build the design
        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewHolder = new ViewHolder();
            final Game game = getItem(position);

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);

                mainViewHolder.gameName = (TextView) convertView.findViewById(R.id.txtgameName);
                mainViewHolder.currentMembers = (TextView) convertView.findViewById(R.id.txtMemberCount);
                mainViewHolder.playGame = (Button) convertView.findViewById(R.id.btnPlayGame);
                mainViewHolder.playGame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        /* After the button 'playGame' has been clicked the discovery will stop. */
                        clientService.stopDiscovery();

                        /* Request connection to server. */
                        Log.d("CLIENT_SERVICE", "Requesting connection.");
                        clientService.connectToEndpoint(endpoints.get(0)); // Temporary. TODO: Find solution for getting endpoint from the list! Suggestion: Link ViewHolder and Endpoint somehow (e.g. add Endpoint field to ViewHolder).
                        clientService.send(myData);

                        /* Start registration activity (enter username). */
                        Log.d("CLIENT_SERVICE", "Loading game map.");
                        Intent intent = new Intent(GameList.this, GameMap.class);
                        startActivity(intent);
                    }
                });
                convertView.setTag(mainViewHolder);

            }

            //set Text from stored variables to views
            mainViewHolder = (ViewHolder) convertView.getTag();
            mainViewHolder.gameName.setText(game.getGameName());
            mainViewHolder.currentMembers.setText(Integer.toString(game.getCurrentMembers()) + "/" + Integer.toString(game.getMaxMembers()));

            //returns finished view
            return convertView;

        }
    }

    //what i need in my view
    public class ViewHolder {
        TextView gameName;
        TextView currentMembers;
        Button playGame;
    }

    @Override
    public void onStartedDiscovery() {
        Log.d("CLIENT_SERVICE", "Started discovery.");
    }

    @Override
    public void onFailedDiscovery() {
        Log.d("CLIENT_SERVICE", "Failed discovery.");
    }

    @Override
    public void onEndpointFound(Map<String, Endpoint> discoveredEndpoints) {
        Log.d("CLIENT_SERVICE", "A new endpoint has been discovered!");

        /* Get list of all connected endpoints and ignore those which were already there - detect the newly found endpoint. */
        ArrayList<Endpoint> discoveredEndpointsList = new ArrayList<>(discoveredEndpoints.values());
        discoveredEndpointsList.removeAll(this.endpoints);
        this.endpoints = new ArrayList<>(discoveredEndpoints.values());

        Endpoint newlyDiscoveredEndpoint = discoveredEndpointsList.get(0);

        Log.d("CLIENT_SERVICE", "New endpoint connected: " + newlyDiscoveredEndpoint.toString() + " - sending request for data ...");
        // games.add(new Game(newlyDiscoveredEndpoint.toString(), 0));

        /* Connect to the newly discovered endpoint (this will automatically 'request' game data from it). */
        clientService.connectToEndpoint(newlyDiscoveredEndpoint);
    }

    @Override
    public void onEndpointLost(Map<String, Endpoint> discoveredEndpoints) {
        Log.d("CLIENT_SERVICE", "Connection with an already discovered endpoint has been lost.");

        /* Get list of all connected endpoints and ignore those which were already there - detect the lost endpoint. */
        ArrayList<Endpoint> discoveredEndpointsList = new ArrayList<>(discoveredEndpoints.values());
        ArrayList<Endpoint> oldEndpointsList = new ArrayList<>(this.endpoints);
        oldEndpointsList.removeAll(discoveredEndpointsList);
        this.endpoints = discoveredEndpointsList;

        Endpoint newlyLostEndpoint = oldEndpointsList.get(0);

        Log.d("CLIENT_SERVICE", "Endpoint disconnected: " + newlyLostEndpoint.toString() + ".");

        // TODO: After method disconnectEndpoint(Endpoint endpoint) in ClientService has been implemented please disconnect the endpoint so it can reconnect later again.
    }

    @Override
    public void onStoppedDiscovery() {
        Log.d("CLIENT_SERVICE", "Stopped discovery.");

        clientService.stopDiscovery();
        Log.d("SERVER_SERVICE", "Discovery stopped successfully. Initiator: onStoppedDiscovery()");

        ((ProgressBar) findViewById(R.id.progressBarDiscovery)).setVisibility(View.GONE);
        ((Button) findViewById(R.id.rediscoverButton)).setVisibility(View.VISIBLE);
    }

    @Override
    public void onConnected(Endpoint endpoint) {
        Log.d("CLIENT_SERVICE", "Connected with " + endpoint.toString() + " successfully.");

        Log.d("CLIENT_SERVICE", "Requesting game data...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                /* For some reason the server doesn't detect the first message. TODO: Check why. */
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                clientService.send(new Message("GET_GAME_DATA"));
            }
        }).start();
    }

    @Override
    public void onGameData(Object game) {
        Log.d("CLIENT_SERVICE", "Game data received!");

        // TODO: After game object has been made unique we can detect Game data updates. Until then we just add updated data as a new game to the list.

        Game gameReceived = (Game) game;

        /* Remove game data from the list and add it again (update) */
        for (Game gameData : this.games) {
            if (gameData.getGameName().equals(gameReceived.getGameName()))
                this.games.remove(gameData);
        }
        games.add(gameReceived);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMessage(Object message) {
        Log.d("CLIENT_SERVICE", "Message data received!");

        String receivedMessage = ((Message) message).getMessage();

        if (receivedMessage.equals("CONNECTION_ACCEPTED")) {
            Log.d("CLIENT_SERVICE", "Connection accepted by server!");

            Intent gameStartIntent = new Intent(GameList.this, GameMap.class);
            startActivity(gameStartIntent);
        }
    }

    @Override
    public void onFailedConnecting(Endpoint endpoint) {
        Log.d("CLIENT_SERVICE", "Connection to " + endpoint.getName() + " failed!");
    }

    @Override
    public void onDisconnected(Endpoint endpoint) {
        Log.d("CLIENT_SERVICE", "Endpoint " + endpoint.getName() + " disconnected successfully!");
    }

    @Override
    public void onFailedAcceptConnection(Endpoint endpoint) {
        Log.d("CLIENT_SERVICE", "Endpoint " + endpoint.getName() + " has failed to accept connection!");
    }

    @Override
    public void onSendingFailed(Object object) {
        Log.d("CLIENT_SERVICE", "Failed to send the message!");
    }
}