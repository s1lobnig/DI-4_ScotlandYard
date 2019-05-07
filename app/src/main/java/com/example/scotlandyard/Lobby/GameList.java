package com.example.scotlandyard.Lobby;

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

import com.example.scotlandyard.Map.GameMap;
import com.example.scotlandyard.Player;
import com.example.scotlandyard.R;
import com.example.scotlandyard.connection.ClientInterface;
import com.example.scotlandyard.connection.ClientService;
import com.example.scotlandyard.connection.Endpoint;
import com.example.scotlandyard.messenger.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameList extends AppCompatActivity implements ClientInterface {
    private ListView gameListView; /* ListView GUI Element */
    private MyListAdapter listAdapter; /* Adapter between ListView and  ArrayList<Game> */
    private ClientService clientService; /* ClientService - used for communication with server(s). */
    private ArrayList<Endpoint> endpoints = new ArrayList<>(); /* List of detected endpoints (servers). */
    private ArrayList<Game> games = new ArrayList<>(); /* List of available games (or game servers). */
    private String userName;
    private Player client;
    private Game myData = new Game("", -1);
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

        /* Create new client service and start discovery. */
        clientService = ClientService.getInstance(this, android.os.Build.MODEL, this);
        clientService.startDiscovery();

        gameListView = findViewById(R.id.list_currentGames);

        //setAdapter to listView to show all existing games
        listAdapter = new MyListAdapter(this, R.layout.game_item, games);
        gameListView.setAdapter(listAdapter);

        /* Get intent data. */
        Intent intent = getIntent();
        String username = intent.getStringExtra("USERNAME");
        client = new Player(userName);
        client.setHost(false);
        myData.getPlayers().add(client);

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
        private String logTag = "CLIENT_SERVICE";

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
                        Log.d(logTag, "Requesting connection.");
                        clientService.connectToEndpoint(endpoints.get(0)); // Temporary. TODO: Find solution for getting endpoint from the list! Suggestion: Link ViewHolder and Endpoint somehow (e.g. add Endpoint field to ViewHolder).
                        clientService.send(myData);

                        /* Start registration activity (enter username). */
                        Log.d(logTag, "Loading game map.");
                        Intent intent = new Intent(GameList.this, GameMap.class);

                        intent.putExtra("CLIENT", client);
                        intent.putExtra("USER_NAME", myData.getPlayers().get(0).getNickname());
                        intent.putExtra("IS_SERVER", false);
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
        Log.d(logTag, "Started discovery.");
    }

    @Override
    public void onFailedDiscovery() {
        Log.d(logTag, "Failed discovery.");
    }

    @Override
    public void onEndpointFound(Map<String, Endpoint> discoveredEndpoints) {
        Log.d(logTag, "A new endpoint has been discovered!");

        /* Get list of all connected endpoints and ignore those which were already there - detect the newly found endpoint. */
        ArrayList<Endpoint> discoveredEndpointsList = new ArrayList<>(discoveredEndpoints.values());
        discoveredEndpointsList.removeAll(this.endpoints);
        this.endpoints = new ArrayList<>(discoveredEndpoints.values());

        Endpoint newlyDiscoveredEndpoint = discoveredEndpointsList.get(0);

        Log.d(logTag, "New endpoint connected: " + newlyDiscoveredEndpoint.toString() + " - sending request for data ...");
        // games.add(new Game(newlyDiscoveredEndpoint.toString(), 0));

        /* Connect to the newly discovered endpoint (this will automatically 'request' game data from it). */
        clientService.connectToEndpoint(newlyDiscoveredEndpoint);
    }

    @Override
    public void onEndpointLost(Map<String, Endpoint> discoveredEndpoints) {
        Log.d(logTag, "Connection with an already discovered endpoint has been lost.");

        /* Get list of all connected endpoints and ignore those which were already there - detect the lost endpoint. */
        ArrayList<Endpoint> discoveredEndpointsList = new ArrayList<>(discoveredEndpoints.values());
        ArrayList<Endpoint> oldEndpointsList = new ArrayList<>(this.endpoints);
        oldEndpointsList.removeAll(discoveredEndpointsList);
        this.endpoints = discoveredEndpointsList;

        Endpoint newlyLostEndpoint = oldEndpointsList.get(0);

        Log.d(logTag, "Endpoint disconnected: " + newlyLostEndpoint.toString() + ".");

        // TODO: After method disconnectEndpoint(Endpoint endpoint) in ClientService has been implemented please disconnect the endpoint so it can reconnect later again.
    }

    @Override
    public void onStoppedDiscovery() {
        Log.d(logTag, "Stopped discovery.");

        clientService.stopDiscovery();
        Log.d(logTag, "Discovery stopped successfully. Initiator: onStoppedDiscovery()");

        ((ProgressBar) findViewById(R.id.progressBarDiscovery)).setVisibility(View.GONE);
        ((Button) findViewById(R.id.rediscoverButton)).setVisibility(View.VISIBLE);
    }

    @Override
    public void onConnected(Endpoint endpoint) {
        Log.d(logTag, "Connected with " + endpoint.toString() + " successfully.");

        Log.d(logTag, "Requesting game data...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                /* For some reason the server doesn't detect the first message. TODO: Check why. */
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Log.e("InterruptedExeption", e.getMessage(), e.getCause());
                }
                clientService.send(new Message("GET_GAME_DATA"));
            }
        }).start();
    }

    @Override
    public void onGameData(Object game) {
        Log.d(logTag, "Game data received!");

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
        Log.d(logTag, "Message data received!");

        String receivedMessage = ((Message) message).getMessage();

        if (receivedMessage.equals("CONNECTION_ACCEPTED")) {
            Log.d(logTag, "Connection accepted by server!");

            Intent gameStartIntent = new Intent(GameList.this, GameMap.class);
            startActivity(gameStartIntent);
        }
    }

    @Override
    public void onSendMove(Object sendMove) {
        Log.d(logTag, "Move received");
    }

    @Override
    public void onFailedConnecting(Endpoint endpoint) {
        Log.d(logTag, "Connection to " + endpoint.getName() + " failed!");
    }

    @Override
    public void onDisconnected(Endpoint endpoint) {
        Log.d(logTag, "Endpoint " + endpoint.getName() + " disconnected successfully!");
    }

    @Override
    public void onFailedAcceptConnection(Endpoint endpoint) {
        Log.d(logTag, "Endpoint " + endpoint.getName() + " has failed to accept connection!");
    }

    @Override
    public void onSendingFailed(Object object) {
        Log.d(logTag, "Failed to send the message!");
    }
}