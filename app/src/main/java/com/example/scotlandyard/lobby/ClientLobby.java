package com.example.scotlandyard.lobby;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
    ArrayList<Player> players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_lobby);

        Intent intent = getIntent();
        Lobby lobby = (Lobby)intent.getSerializableExtra("LOBBY");
        players = new ArrayList<>();

        ListView connectedPlayersList = (ListView) findViewById(R.id.playersList);

        connectedPlayersListAdapter = new ArrayAdapter<Player>(this, android.R.layout.simple_list_item_1, players);
        connectedPlayersList.setAdapter(connectedPlayersListAdapter);

        ((Client) Device.getInstance()).addLobbyObserver(this);
        updateLobby(lobby);
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

        ((TextView) findViewById(R.id.textConnectionInfo)).setText("Das Spiel wird gestartet...");
        ((ProgressBar) findViewById(R.id.progressBarConnection)).setProgress(100);

        Intent gameStartIntent = new Intent(ClientLobby.this, GameMap.class);
        startActivity(gameStartIntent);
    }

    @Override
    public void showConnectionFailed(String endpointName) {
        Log.d(logTag, "should not happen");
    }

    @Override
    public void showDisconnected(String endpointName) {
        Log.d(logTag, "onDisconnected() : " + endpointName);

        String notification = "Verbindung zum Server verloren.";
        Toast.makeText(getApplicationContext(), notification, Toast.LENGTH_LONG).show();

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

        ((CheckBox) findViewById(R.id.randomEvents)).setChecked(lobby.isRandomEvents());
        ((CheckBox) findViewById(R.id.randomMrX)).setChecked(lobby.isRandomMrX());
        ((CheckBox) findViewById(R.id.botMrX)).setChecked(lobby.isBotMrX());

        ((CheckBox) findViewById(R.id.randomEvents)).setEnabled(false);
        ((CheckBox) findViewById(R.id.randomMrX)).setEnabled(false);
        ((CheckBox) findViewById(R.id.botMrX)).setEnabled(false);


        players.clear();
        players.addAll(lobby.getPlayerList());
        ((ArrayAdapter) connectedPlayersListAdapter).notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
