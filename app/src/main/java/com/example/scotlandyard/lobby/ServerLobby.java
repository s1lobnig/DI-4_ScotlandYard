package com.example.scotlandyard.lobby;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.scotlandyard.control.Device;
import com.example.scotlandyard.control.Server;
import com.example.scotlandyard.control.ServerLobbyInterface;
import com.example.scotlandyard.map.GameMap;
import com.example.scotlandyard.Player;
import com.example.scotlandyard.R;
import com.example.scotlandyard.connection.Endpoint;
import com.google.android.gms.nearby.Nearby;

public class ServerLobby extends AppCompatActivity implements ServerLobbyInterface {
    private String logTag = "ServerLobby";
    private ListAdapter connectedPlayersListAdapter;
    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Button startGameButton = (Button) findViewById(R.id.startGameBtn);
        ListView connectedPlayersList = (ListView) findViewById(R.id.playersList);

        /* Get intent data. */
        Intent intent = getIntent();
        Lobby lobby = (Lobby) intent.getSerializableExtra("LOBBY");
        player = (Player) intent.getSerializableExtra("PLAYER");

        //TODO show lobby information in this activity (maxPlayers, randomMr.X, randomEvents)
        //maxPlayers in what form?

        ((CheckBox) findViewById(R.id.randomEvents)).setChecked(lobby.isRandomEvents());
        ((CheckBox) findViewById(R.id.randomMrX)).setChecked(lobby.isRandomMrX());
        ((CheckBox) findViewById(R.id.botMrX)).setChecked(lobby.isBotMrX());

        ((CheckBox) findViewById(R.id.randomEvents)).setEnabled(false);
        ((CheckBox) findViewById(R.id.randomMrX)).setEnabled(false);
        ((CheckBox) findViewById(R.id.botMrX)).setEnabled(false);

        if (Device.isSingletonSet()) {
            Device.resetInstance();
        }
        try {
            Server server = (Server) Device.setServer(lobby.getLobbyName(), Nearby.getConnectionsClient(this));
            server.setLobby(lobby);
            server.addLobbyObserver(this);
            server.setNickname(lobby.getPlayerList().get(0).getNickname());
            server.startAdvertising();
        } catch (IllegalStateException ex) {
            Log.d(logTag, "failed setting instance", ex);
        }

        /* Start game initiated by server. */
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(logTag, "Loading game map.");
                if(Device.getInstance().getLobby().isBotMrX()) {
                    Device.getInstance().getLobby().addPlayer(new Player("Bot"));
                }

                Intent intent = new Intent(ServerLobby.this, GameMap.class);
                //Game game = new Game(((Server) Device.getInstance()).getLobby().getLobbyName(), ((Server) Device.getInstance()).getLobby().getMaxPlayers());
                //game.setPlayers(((Server) Device.getInstance()).getLobby().getPlayerList());
                //TODO write lobby information to the game
                startActivity(intent);
            }
        });

        connectedPlayersListAdapter = new ArrayAdapter<Player>(this, android.R.layout.simple_list_item_1, lobby.getPlayerList());
        connectedPlayersList.setAdapter(connectedPlayersListAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ((Server) Device.getInstance()).removeLobbyObserver();
        ((Server) Device.getInstance()).stopAdvertising();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((Server) Device.getInstance()).removeLobbyObserver();
        ((Server) Device.getInstance()).stopAdvertising();
    }

    public void showFailedAdvertising() {
        //TODO show user that advertising failed
    }

    @Override
    public void showStoppedAdvertising() {
        //TODO show user that advertising stopped
    }

    @Override
    public void showConnectionRequest(Endpoint endpoint) {
        //TODO ask user, if he wants to accept or reject the connection of that endpoint
        ((Server) Device.getInstance()).acceptConnection(endpoint);     //accepts the connection
        // ((Server) Device.getInstance()).acceptConnection(endpoint);  //rejects the connection
    }

    @Override
    public void showConnectionFailed(String endpointName) {
        //TODO show user that connection to endpoint has failed

        String notification = "Verbindung zum Server konnte nicht hergestellt werden.";
        Toast.makeText(getApplicationContext(), notification, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showDisconnected(String endpointName) {
        Log.d(logTag, "should not be called.");

        String notification = "Verbindung zum Server verloren.";
        Toast.makeText(getApplicationContext(), notification, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showAcceptingFailed(String endpointName) {
        //TODO show user that connection accept to endpoint has failed

        String notification = "Verbindung zum Server konnte nicht hergestellt werden.";
        Toast.makeText(getApplicationContext(), notification, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSendingFailed(Object object) {
        //TODO show user that sending of that object has failed
    }

    @Override
    public void updateLobby(Lobby lobby) {
        ((ArrayAdapter) connectedPlayersListAdapter).notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
