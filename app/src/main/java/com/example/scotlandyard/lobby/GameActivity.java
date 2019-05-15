package com.example.scotlandyard.lobby;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.scotlandyard.Server;
import com.example.scotlandyard.map.GameMap;
import com.example.scotlandyard.map.roadmap.Entry;
import com.example.scotlandyard.messenger.Message;
import com.example.scotlandyard.Player;
import com.example.scotlandyard.R;
import com.example.scotlandyard.map.motions.SendMove;
import com.example.scotlandyard.connection.Endpoint;
import com.example.scotlandyard.connection.ServerInterface;
import com.example.scotlandyard.connection.ServerService;
import com.google.android.gms.nearby.Nearby;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    private String logTag = "GameActivity";

    private ListAdapter connectedPlayersListAdapter;
    private String userName;
    private boolean chooseMrXRandomly;
    private boolean randomEventsEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Button startGameButton = (Button) findViewById(R.id.startGameBtn);
        ListView connectedPlayersList = (ListView) findViewById(R.id.playersList);

        /* Get intent data. */
        Intent intent = getIntent();
        Lobby lobby = (Lobby)intent.getSerializableExtra("LOBBY");

        if (Server.isSingletonSet()) {
            Server.resetInstance();
        }
        try {
            serverService = ServerService.setInstance(GameActivity.this, userName + "'s game server", Nearby.getConnectionsClient(this));
        } catch (IllegalStateException ex) {
            Log.d(logTag, "failed setting instance", ex);
        }
        serverService.startAdvertising();

        /* This is info about the game - it will be sent to clients when they connect. */
        game = new Game(userName + "'s game server", maxPlayers);
        final Player host = new Player(userName);
        host.setHost(true);
        game.getPlayers().add(host);
        game.setCurrentMembers(1); /* The server is also a player. */


        /* Start game initiated by server. */
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.chooseMrX(chooseMrXRandomly);
                Log.d("GAME_ACTIVITY", "Loading game map.");

                serverService.send(new Message("START_GAME"));

                Intent intent = new Intent(GameActivity.this, GameMap.class);
                intent.putExtra("USERNAME", userName);
                intent.putExtra("HOST", host);

                intent.putExtra("GAME", game);
                intent.putExtra("IS_SERVER", true);

                intent.putExtra("RANDOM_EVENTS", randomEventsEnabled);

                startActivity(intent);
            }
        });

        connectedPlayersListAdapter = new ArrayAdapter<Player>(this, android.R.layout.simple_list_item_1, game.getPlayers());
        connectedPlayersList.setAdapter(connectedPlayersListAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onStartedAdvertising() {
        Log.d(logTag, "Started advertising.");
    }

    @Override
    public void onFailedAdvertising() {
        //TODO give user feedback, that advertising failed
        // Make a Toast.
        Log.d(logTag, "Failed advertising.");
    }

    @Override
    public void onStoppedAdvertising() {
        Log.d(logTag, "Stopped advertising.");
    }

    @Override
    public void onConnectionRequested(Endpoint endpoint) {
        Log.d(logTag, endpoint.toString() + " has requested connection.");

        if (this.game.getCurrentMembers() < this.game.getMaxMembers()) {
            if (!this.game.nickAlreadyUsed(endpoint.getName())) {
                //TODO: maybe let server player decide, if that endpoint (player) is allowed to join
                serverService.acceptConnection(endpoint);
            } else {
                Log.d(logTag, "nick already in use");
                serverService.rejectConnection(endpoint);
            }
        } else {
            Log.d(logTag, "lobby full");
            serverService.rejectConnection(endpoint);
            serverService.stopAdvertising();
        }
    }

    @Override
    public void onConnected(Endpoint establishedConnection) {
        Log.d(logTag, "Connection with a new endpoint established.");

        Player newPlayer = new Player(establishedConnection.getName());
        this.game.getPlayers().add(newPlayer);

        this.game.setCurrentMembers(this.game.getCurrentMembers() + 1);

        ((ArrayAdapter) connectedPlayersListAdapter).notifyDataSetChanged();

        Log.d("SERVER_SERVICE", "Sending game data to all clients.");
        serverService.send(this.game);
    }

    @Override
    public void onGameData(Game game) {
        Log.d(logTag, "should not get any game data");
    }

    @Override
    public void onMessage(Message message) {
        Log.d(logTag, "message received.");

        if (message.toString().startsWith("GET_GAME_DATA")) {
            Log.d(logTag, "Sending game data to users.");
            serverService.send((Game) this.game);
        }
    }

    @Override
    public void onSendMove(SendMove sendMove) {
        Log.d(logTag, "should not get any move");
    }

    @Override
    public void onRoadMapEntry(Entry entry) {

    }

    @Override
    public void onFailedConnecting(Endpoint endpoint) {
        //TODO inform player, that connecting has failed
        // Make a Toast: Couldn't connect with endpoint.
        Log.d(logTag, "Connection with " + endpoint.toString() + " failed!");
    }

    @Override
    public void onDisconnected(Endpoint endpoint) {
        // TODO display that player has left the lobby (update the list and game data)
        // Detect which player has left and remove it from the game.getPlayers() list.

        Log.d(logTag, endpoint.toString() + " disconnected successfully!");

        Player playerToRemove = null;
        for (Player player :
                game.getPlayers()) {
            if (player.getNickname().equals(endpoint.getName()))
                playerToRemove = player;
        }
        game.getPlayers().remove(playerToRemove);

        this.game.setCurrentMembers(this.game.getCurrentMembers() - 1);

        ((ArrayAdapter) connectedPlayersListAdapter).notifyDataSetChanged();

        Log.d("SERVER_SERVICE", "Sending game data to all clients.");
        serverService.send(this.game);
    }

    @Override
    public void onFailedAcceptConnection(Endpoint endpoint) {
        //TODO inform player, that accepting connection has failed
        // Make a Toast
        Log.d(logTag, endpoint.toString() + " failed to accept connection!");
    }

    @Override
    public void onSendingFailed(Object object) {
        //TODO inform player, that sending has failed
        // Make a Toast
        Log.d(logTag, "Sending data failed!");
    }
}
