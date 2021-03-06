package com.example.scotlandyard.lobby;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scotlandyard.Game;
import com.example.scotlandyard.control.Client;
import com.example.scotlandyard.control.ClientLobbyInterface;
import com.example.scotlandyard.control.Device;
import com.example.scotlandyard.Player;
import com.example.scotlandyard.R;
import com.example.scotlandyard.connection.Endpoint;
import com.google.android.gms.nearby.Nearby;

import java.util.ArrayList;

public class GameList extends AppCompatActivity implements ClientLobbyInterface {
    private Player player;
    private ArrayAdapter listAdapter; /* Adapter between ListView and  ArrayList<Game> */

    private static final String TAG = "GameList";

    @Override
    protected void onStop() {
        super.onStop();

        ((ProgressBar) findViewById(R.id.progressBarDiscovery)).setVisibility(View.GONE);
        ((Button) findViewById(R.id.rediscoverButton)).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.textInfo)).setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ((Client) Device.getInstance()).removeLobbyObserver();

        ((ProgressBar) findViewById(R.id.progressBarDiscovery)).setVisibility(View.GONE);
        ((Button) findViewById(R.id.rediscoverButton)).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.textInfo)).setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Get intent data. */
        Intent intent = getIntent();
        player = (Player) intent.getSerializableExtra("PLAYER");

        if (Device.isSingletonSet()) {
            Device.resetInstance();
        }
        try {
            Client client = (Client) Device.setClient(player.getNickname(), Nearby.getConnectionsClient(this));
            client.addLobbyObserver(this);
            client.startDiscovery();
        } catch (IllegalStateException ex) {
            Log.d(TAG, "failed setting instance", ex);
        }

        ListView gameListView = findViewById(R.id.list_currentGames);

        //setAdapter to listView to show all existing games
        listAdapter = new MyListAdapter(this, R.layout.game_item, ((Client) Device.getInstance()).getServerList());
        gameListView.setAdapter(listAdapter);

        /* Called when clicked on an item from the list of available servers. */
        gameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((Client) Device.getInstance()).connectToEndpoint(position);
            }
        });


        findViewById(R.id.rediscoverButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ProgressBar) findViewById(R.id.progressBarDiscovery)).setVisibility(View.VISIBLE);
                ((Button) findViewById(R.id.rediscoverButton)).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.textInfo)).setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.progressBarDiscovery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ProgressBar) findViewById(R.id.progressBarDiscovery)).setVisibility(View.GONE);
                ((Button) findViewById(R.id.rediscoverButton)).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.textInfo)).setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void showStartedDiscovering() {
        ((ProgressBar) findViewById(R.id.progressBarDiscovery)).setVisibility(View.VISIBLE);
        ((Button) findViewById(R.id.rediscoverButton)).setVisibility(View.GONE);
        ((TextView) findViewById(R.id.textInfo)).setVisibility(View.VISIBLE);
    }

    @Override
    public void showFailedDiscovering() {
        String notification = "Es konnte kein Server gefunden werden (das Gerät unterstützt die Nearby API nicht oder die Ressourcen wurden bereits verwendet).";
        Toast.makeText(getApplicationContext(), notification, Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateServerList(ArrayList<Endpoint> serverList) {
        listAdapter.clear();
        listAdapter.addAll(serverList);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void showStoppedDiscovery() {
        ((ProgressBar) findViewById(R.id.progressBarDiscovery)).setVisibility(View.GONE);
        ((Button) findViewById(R.id.rediscoverButton)).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.textInfo)).setVisibility(View.GONE);
    }

    @Override
    public void showConnected(String endpointName) {
        Log.d(TAG, "Verbunden, warten auf Lobby-Beitritt.");
    }

    @Override
    public void startGame(Game game) {
        Log.d(TAG, "should not happen here");
    }

    @Override
    public void showConnectionFailed(String endpointName) {
        Log.d(TAG, "Verbundindung fehlgeschlagen.");
    }

    @Override
    public void showDisconnected(String endpointName) {
        Log.d(TAG, "Verbundindung abgebaut.");
    }

    @Override
    public void showAcceptingFailed(String endpointName) {
        Log.d(TAG, "Verbindung nicht akzeptiert");
    }

    @Override
    public void showSendingFailed(Object object) {
        Log.d(TAG, "Senden fehlgeschlagen");
    }

    @Override
    public void updateLobby(Lobby lobby) {
        ((Client) Device.getInstance()).removeLobbyObserver();
        Device.getInstance().setNickname(player.getNickname());

        /* Start Client Lobby activity. */
        Log.d(TAG, "Loading client lobby.");
        Intent intent = new Intent(GameList.this, ClientLobby.class);
        intent.putExtra("LOBBY", lobby);
        startActivity(intent);
    }

    //need adapter to design a list item and add it to list
    private class MyListAdapter extends ArrayAdapter<Endpoint> {
        private int layout;

        public MyListAdapter(GameList context, int resource, ArrayList<Endpoint> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        //build the design
        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            GameListViewHolder mainViewHolder = new GameListViewHolder();
            final Endpoint game = getItem(position);


            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);

                mainViewHolder.gameName = (TextView) convertView.findViewById(R.id.txtgameName);
                mainViewHolder.playGame = (Button) convertView.findViewById(R.id.btnPlayGame);
                mainViewHolder.playGame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Request connection to server.

                        ((Client) Device.getInstance()).connectToEndpoint(position);


                    }
                });
                convertView.setTag(mainViewHolder);

            }

            //set Text from stored variables to views
            mainViewHolder = (GameListViewHolder) convertView.getTag();
            mainViewHolder.gameName.setText(game.getName());

            //returns finished view
            return convertView;

        }
    }
}
