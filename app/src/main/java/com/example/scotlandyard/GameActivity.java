package com.example.scotlandyard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.scotlandyard.connection.Endpoint;
import com.example.scotlandyard.connection.ServerInterface;
import com.example.scotlandyard.connection.ServerService;

import java.util.ArrayList;
import java.util.Map;

public class GameActivity extends AppCompatActivity implements ServerInterface {

    private ServerService serverService; /* ServerService - used for communication with client(s). */
    private ArrayList<Endpoint> endpoints = new ArrayList<>(); /* List of detected endpoints (clients). */
    private Game game = new Game("NOT INITIALIZED", 4); /* Game info/data. */

    private ListAdapter connectedPlayersListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Button startGameButton = (Button) findViewById(R.id.startGameBtn);
        ListView connectedPlayersList = (ListView) findViewById(R.id.playersList);

        /* Get intent data. */
        Intent intent = getIntent();
        String serverName = intent.getStringExtra("SERVER_NAME");
        final String userName = intent.getExtras().getString("USER_NAME");
        int maxPlayers = intent.getExtras().getInt("MAX_PLAYERS");
        boolean buttonEnabled = intent.getExtras().getBoolean("ENABLE_BUTTON");

        /* Start ServerService and start advertising own endpoint. */
        serverService = new ServerService(GameActivity.this, userName + "'s game server", GameActivity.this);
        serverService.startAdvertising();

        /* This is info about the game - it will be sent to clients when they connect. */
        game = new Game(serverName, maxPlayers);
        game.getPlayers().add(new Player(userName));
        game.setCurrentMembers(1); /* The server is also a player. */

        /* Start game initiated by server. */
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("GAME_ACTIVITY", "Loading game map.");
                Intent intent = new Intent(GameActivity.this, GameMap.class);
                intent.putExtra("USERNAME", userName);
                startActivity(intent);
            }
        });

        connectedPlayersListAdapter = new ArrayAdapter<Player>(this, android.R.layout.simple_list_item_1, game.getPlayers());
        connectedPlayersList.setAdapter(connectedPlayersListAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        serverService.stopAdvertising();
        Log.d("SERVER_SERVICE", "Advertising stopped successfully. Initiator: stopDiscovery()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        serverService.stopAdvertising();
        Log.d("SERVER_SERVICE", "Advertising stopped successfully. Initiator: onDestroy()");
    }

    @Override
    public void onStartedAdvertising() {
        Log.d("SERVER_SERVICE", "Started advertising.");
    }

    @Override
    public void onFailedAdvertising() {
        Log.d("SERVER_SERVICE", "Failed advertising.");

    }

    @Override
    public void onStoppedAdvertising() {
        Log.d("SERVER_SERVICE", "Stopped advertising.");
    }

    @Override
    public void onConnectionRequested(Endpoint endpoint) {
        Log.d("SERVER_SERVICE", endpoint.toString() + " has requested connection.");

        /* If number of maximum players not exceeded then accept connection with the endpoint. */
        // if (this.game.getCurrentMembers() < this.game.getMaxMembers())
        serverService.acceptConnection(endpoint);
    }

    @Override
    public void onConnected(Map<String, Endpoint> establishedConnections) {
        Log.d("SERVER_SERVICE", "Connection with a new endpoint established.");

        /* Get list of all connected endpoints and ignore those which were already there - detect the newly connected endpoint. */
        ArrayList<Endpoint> establishedConnectionsList = new ArrayList<>(establishedConnections.values());
        establishedConnectionsList.removeAll(this.endpoints);
        this.endpoints = new ArrayList<>(establishedConnections.values());

        Endpoint newlyConnectedEndpoint = establishedConnectionsList.get(0);

        Log.d("SERVER_SERVICE", newlyConnectedEndpoint.toString() + " connected successfully.");
    }

    @Override
    public void onGameData(Object game) {
        Log.d("SERVER_SERVICE", "Game data received from client (new client wants to connect to the server)!");

        if (this.game.getCurrentMembers() < this.game.getMaxMembers()) {
            this.game.getPlayers().add(((Game) game).getPlayers().get(0));
            this.game.setCurrentMembers(this.game.getCurrentMembers() + 1);

            ((ArrayAdapter) connectedPlayersListAdapter).notifyDataSetChanged();

            Log.d("SERVER_SERVICE", "Sending game data to all clients.");
            serverService.send((Game) this.game);
        }
    }

    @Override
    public void onMessage(Object message) {
        Log.d("SERVER_SERVICE", "Message data received!");

        String receivedMessage = ((Message) message).getMessage();
        Log.d("SERVER_SERVICE", "Received message: " + receivedMessage);

        if (receivedMessage.startsWith("GET_GAME_DATA")) {
            Log.d("SERVER_SERVICE", "Sending game data to users.");
            serverService.send((Game) this.game);
        }
    }

    @Override
    public void onFailedConnecting(Endpoint endpoint) {
        Log.d("SERVER_SERVICE", "Connection with " + endpoint.toString() + " failed!");
    }

    @Override
    public void onDisconnected(Endpoint endpoint) {
        Log.d("SERVER_SERVICE", endpoint.toString() + " disconnected successfully!");

        // TODO: After method disconnectEndpoint(Endpoint endpoint) in ServerService has been implemented please disconnect the endpoint so it can reconnect later again.
    }

    @Override
    public void onFailedAcceptConnection(Endpoint endpoint) {
        Log.d("SERVER_SERVICE", endpoint.toString() + " failed to accept connection!");
    }

    @Override
    public void onSendingFailed(Object object) {
        Log.d("SERVER_SERVICE", "Sending data failed!");
    }
}
