package com.example.scotlandyard.lobby;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.scotlandyard.control.Client;
import com.example.scotlandyard.control.ClientLobbyInterface;
import com.example.scotlandyard.control.Device;
import com.example.scotlandyard.map.GameMap;
import com.example.scotlandyard.Player;
import com.example.scotlandyard.R;
import com.example.scotlandyard.connection.Endpoint;

import java.util.ArrayList;

public class ClientLobby extends AppCompatActivity implements ClientLobbyInterface {
    private ListAdapter connectedPlayersListAdapter; /* Global variable because of updates. */
    private String logTag = "ClientLobby";
    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_lobby);

        Intent intent = getIntent();
        Lobby lobby = (Lobby)intent.getSerializableExtra("LOBBY");
        player = (Player)intent.getSerializableExtra("PLAYER");

        ListView connectedPlayersList = (ListView) findViewById(R.id.playersList);

        connectedPlayersListAdapter = new ArrayAdapter<Player>(this, android.R.layout.simple_list_item_1, lobby.getPlayerList());
        connectedPlayersList.setAdapter(connectedPlayersListAdapter);

        updateLobby(lobby);
        ((Client) Device.getInstance()).addLobbyObserver(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((Client) Device.getInstance()).removeLobbyObserver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ((Client) Device.getInstance()).removeLobbyObserver();
    }

    @Override
    public void showStartedDiscovering() {
        Log.d(logTag, "should not happen");
    }

    @Override
    public void showFailedDiscovering() {
        Log.d(logTag, "should not happen");
    }

    @Override
    public void updateServerList(ArrayList<Endpoint> serverList) {
        Log.d(logTag, "should not happen");
    }

    @Override
    public void showStoppedDiscovery() {
        Log.d(logTag, "should not happen");
    }

    @Override
    public void showConnected(String endpointName) {
        Log.d(logTag, "should not happen");
    }

    @Override
    public void startGame(Game game) {
        Log.d(logTag, "Game start initiated by server!");
        Intent gameStartIntent = new Intent(ClientLobby.this, GameMap.class);
        gameStartIntent.putExtra("USERNAME", player.getNickname());
        gameStartIntent.putExtra("IS_SERVER", false);
        startActivity(gameStartIntent);
    }

    @Override
    public void showConnectionFailed(String endpointName) {
        Log.d(logTag, "should not happen");
    }

    @Override
    public void showDisconnected(String endpointName) {
        //TODO show user that we have disconnected
        Log.d(logTag, "onDisconnected() : " + endpointName);

        String notification = "Verbindung zum Server verloren.";
        Toast.makeText(getApplicationContext(), notification, Toast.LENGTH_LONG).show();

        finish();
    }

    @Override
    public void showAcceptingFailed(String endpointName) {
        Log.d(logTag, "should not happen");
    }

    @Override
    public void showSendingFailed(Object object) {
        Log.d(logTag, "should not happen");
    }

    @Override
    public void updateLobby(Lobby lobby) {
        //TODO show lobby information in this activity (joined players, maxPlayers, randomMr.X, randomEvents)
        ((ArrayAdapter) connectedPlayersListAdapter).notifyDataSetChanged();
    }
}
