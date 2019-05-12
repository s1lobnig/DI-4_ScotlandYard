package com.example.scotlandyard.lobby;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.scotlandyard.Player;
import com.example.scotlandyard.R;
import com.example.scotlandyard.connection.ClientInterface;
import com.example.scotlandyard.connection.ClientService;
import com.example.scotlandyard.connection.Endpoint;
import com.example.scotlandyard.map.GameMap;
import com.example.scotlandyard.map.motions.SendMove;
import com.example.scotlandyard.map.roadmap.Entry;
import com.example.scotlandyard.messenger.Message;

import java.util.Map;

public class ClientLobby extends AppCompatActivity implements ClientInterface {

    private ClientService clientService; /* ClientService - used for communication with server(s). */
    private ListAdapter connectedPlayersListAdapter; /* Global variable because of updates. */
    private Game game = new Game("NOT_INITIALZED", 0);
    private String logTag = "CLIENT_LOBBY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_lobby);

        Log.d(logTag, "onCreate()");

        clientService = ClientService.getInstance();
        clientService.setClient(this);

        Intent intent = getIntent();

        String username = intent.getStringExtra("USER_NAME");
        String endpointName = intent.getStringExtra("ENDPOINT_NAME");
        String endpointID = intent.getStringExtra("ENDPOINT_ID");

        Endpoint serverEndpoint = new Endpoint(endpointID, endpointName);
        clientService.connectToEndpoint(serverEndpoint);
        Log.d(logTag, "Connecting with " + serverEndpoint.toString());

        ListView connectedPlayersList = (ListView) findViewById(R.id.playersList);

        game.getPlayers().add(new Player(username));

        connectedPlayersListAdapter = new ArrayAdapter<Player>(this, android.R.layout.simple_list_item_1, game.getPlayers());
        connectedPlayersList.setAdapter(connectedPlayersListAdapter);
    }

    /* Not used. */
    @Override
    public void onStartedDiscovery() {
        Log.d(logTag, "onStartedDiscovery()");
    }

    /* Not used. */
    @Override
    public void onFailedDiscovery() {
        Log.d(logTag, "onFailedDiscovery()");
    }

    /* Not used. */
    @Override
    public void onEndpointFound(Map<String, Endpoint> discoveredEndpoints) {
        Log.d(logTag, "onEndpointFound()");
    }

    /* Not used. */
    @Override
    public void onEndpointLost(Map<String, Endpoint> discoveredEndpoints) {
        Log.d(logTag, "onEndpointLost()");
    }

    /* Not used. */
    @Override
    public void onStoppedDiscovery() {
        Log.d(logTag, "onStoppedDiscovery()");
    }

    @Override
    public void onConnected(Endpoint endpoint) {
        Log.d(logTag, "onConnected() : " + endpoint.toString());


        Log.d("CLIENT_LOBBY", "Requesting game data...");
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
    public void onGameData(Game game) {
        Log.d(logTag, "onGameData() : Name = " + game.getGameName() + "; #Players = " + game.getPlayers());

        Game gameReceived = (Game) game;

        this.game.getPlayers().clear();

        this.game.getPlayers().addAll(gameReceived.getPlayers());

        ((ArrayAdapter) connectedPlayersListAdapter).notifyDataSetChanged();
    }

    @Override
    public void onMessage(Message message) {
        Log.d(logTag, "onMessage() : Message = " + message.toString());

        String receivedMessage = ((Message) message).getMessage();

        if (receivedMessage.equals("START_GAME")) {
            Log.d(logTag, "Game start initiated by server!");

            Intent gameStartIntent = new Intent(ClientLobby.this, GameMap.class);
            startActivity(gameStartIntent);
        }
    }

    @Override
    public void onSendMove(SendMove sendMove) {
        Log.d(logTag, "onSendMove() : Message = " + sendMove.toString());

    }

    @Override
    public void onRoadMapEntry(Entry entry) {

    }

    /* Not used. */
    @Override
    public void onFailedConnecting(Endpoint endpoint) {
        Log.d(logTag, "onFailedConnecting() : " + endpoint.toString());

        //TODO show user, that connecting has failed
        // Make a Toast: Couldn't connect to the server.

        finish();
    }

    @Override
    public void onDisconnected(Endpoint endpoint) {
        Log.d(logTag, "onDisconnected() : " + endpoint.toString());

        // Make a Toast: Connection with server lost.

        finish();
    }

    /* Not used. */
    @Override
    public void onFailedAcceptConnection(Endpoint endpoint) {
        Log.d(logTag, "onFailedAcceptConnection() : " + endpoint.toString());

        //TODO show user, that accepting the connection has failed
        // Make a Toast: Couldn't connect to the server.

        finish();
    }

    @Override
    public void onSendingFailed(Object object) {
        Log.d(logTag, "onSendingFailed()");

        //TODO show user, that sending has failed
    }
}