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

import com.example.scotlandyard.Game;
import com.example.scotlandyard.control.Device;
import com.example.scotlandyard.control.Server;
import com.example.scotlandyard.control.ServerLobbyInterface;
import com.example.scotlandyard.map.GameMap;
import com.example.scotlandyard.Player;
import com.example.scotlandyard.R;
import com.example.scotlandyard.connection.Endpoint;
import com.google.android.gms.nearby.Nearby;

public class ServerLobby extends AppCompatActivity implements ServerLobbyInterface {
    private static final String TAG = "ServerLobby";
    private ListAdapter connectedPlayersListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Button startGameButton = (Button) findViewById(R.id.startGameBtn);
        ListView connectedPlayersList = (ListView) findViewById(R.id.playersList);

        /* Get intent data. */
        Intent intent = getIntent();
        Lobby lobby = (Lobby) intent.getSerializableExtra("LOBBY");

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
            Log.d(TAG, "failed setting instance", ex);
        }

        /* Start game initiated by server. */
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Loading game map.");
                if (Device.getInstance().getLobby().isBotMrX()) {
                    Device.getInstance().getLobby().addPlayer(new Player("Bot"));
                }

                Intent intent = new Intent(ServerLobby.this, GameMap.class);
                Lobby lobby = Device.getInstance().getLobby();
                lobby.chooseMrX(lobby.isRandomMrX());
                Device.getInstance().setGame(new Game(lobby.getLobbyName(), lobby.getMaxPlayers(), 1, lobby.isRandomEvents(), lobby.isBotMrX(), lobby.getPlayerList()));
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
        Log.d(TAG, "Advertising fehlgeschlagen");
    }

    @Override
    public void showStoppedAdvertising() {
        Log.d(TAG, "Advertising gestopt");
    }

    @Override
    public void showConnectionRequest(Endpoint endpoint) {
        ((Server) Device.getInstance()).acceptConnection(endpoint);     //accepts the connection
    }

    @Override
    public void showConnectionFailed(String endpointName) {
        Log.d(TAG,"Verbindung zum Server konnte nicht hergestellt werden.");
    }

    @Override
    public void showDisconnected(String endpointName) {
        Log.d(TAG, "should not be called.");
        String notification = "Verbindung zum Server verloren.";
        Toast.makeText(getApplicationContext(), notification, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showAcceptingFailed(String endpointName) {
        String notification = "Verbindung zum Server konnte nicht hergestellt werden.";
        Toast.makeText(getApplicationContext(), notification, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSendingFailed(Object object) {
        Log.d(TAG,"Senden fehlgeschlagen");
    }

    @Override
    public void updateLobby(Lobby lobby) {
        ((ArrayAdapter) connectedPlayersListAdapter).notifyDataSetChanged();
    }
}