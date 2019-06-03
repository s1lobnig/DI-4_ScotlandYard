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

import com.example.scotlandyard.control.Client;
import com.example.scotlandyard.control.ClientLobbyInterface;
import com.example.scotlandyard.control.Device;
import com.example.scotlandyard.map.roadmap.Entry;
import com.example.scotlandyard.messenger.Message;
import com.example.scotlandyard.Player;
import com.example.scotlandyard.R;
import com.example.scotlandyard.map.motions.Move;
import com.example.scotlandyard.connection.ClientInterface;
import com.example.scotlandyard.connection.ClientService;
import com.example.scotlandyard.connection.Endpoint;
import com.google.android.gms.nearby.Nearby;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class GameList extends AppCompatActivity implements ClientLobbyInterface {
    private Player player;
    private ListView gameListView; /* ListView GUI Element */
    private ArrayAdapter listAdapter; /* Adapter between ListView and  ArrayList<Game> */

    private String logTag = "GameList";

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
            Log.d(logTag, "failed setting instance", ex);
        }

        gameListView = findViewById(R.id.list_currentGames);

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
        /*String notification = "Sie sind verbunden und warten darauf, der Lobby beizutreten.";
        Toast.makeText(getApplicationContext(), notification, Toast.LENGTH_LONG).show();*/
    }

    @Override
    public void startGame(Game game) {
        Log.d(logTag, "should not happen here");
    }

    @Override
    public void showConnectionFailed(String endpointName) {
        //TODO show user that connecting to that endpoint has failed
    }

    @Override
    public void showDisconnected(String endpointName) {
        //TODO show user that we have disconnected from that endpoint
    }

    @Override
    public void showAcceptingFailed(String endpointName) {
        //TODO show user that accepting connection of that endpoint has failed
    }

    @Override
    public void showSendingFailed(Object object) {
        //TODO show user that sending of that object has failed
    }

    @Override
    public void updateLobby(Lobby lobby) {
        ((Client) Device.getInstance()).removeLobbyObserver();
        Device.getInstance().setNickname(player.getNickname());

        /* Start Client Lobby activity. */
        Log.d(logTag, "Loading client lobby.");
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
            ViewHolder mainViewHolder = new ViewHolder();
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
            mainViewHolder = (ViewHolder) convertView.getTag();
            mainViewHolder.gameName.setText(game.getName());

            //returns finished view
            return convertView;

        }
    }

    //what i need in my view
    public class ViewHolder {
        TextView gameName;
        Button playGame;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
