package com.example.scotlandyard.lobby;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.scotlandyard.Player;

import com.example.scotlandyard.R;

import java.util.ArrayList;

public class GameCreate extends AppCompatActivity {

    private static final String TAG = "GameCreate";
    private EditText userName; /* User name input field. */
    private EditText maxPlayers; /* Maximum number of players input field. */
    private CheckBox randomEvents;
    private CheckBox chooseMrXRandomly;
    private CheckBox botMrX;
    private EditText lobbyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_create);

        userName = findViewById(R.id.userName);
        maxPlayers = findViewById(R.id.maxPlayers);
        randomEvents = findViewById(R.id.randomEvents);
        chooseMrXRandomly = findViewById(R.id.randomMrX);
        botMrX = findViewById(R.id.botMrX);
        lobbyName = findViewById(R.id.lobbyName);

        botMrX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (botMrX.isChecked()) {
                    chooseMrXRandomly.setChecked(false);
                }
            }
        });

        chooseMrXRandomly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseMrXRandomly.isChecked()) {
                    botMrX.setChecked(false);
                }
            }
        });

        findViewById(R.id.createServerBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String numPlayer = maxPlayers.getText().toString();
                String nickname = userName.getText().toString().trim();
                String lobbyname = lobbyName.getText().toString();
                String[] splittedN = nickname.split(" ");
                boolean enable = checkIfValid(numPlayer, nickname, lobbyname, splittedN);

                if (enable) {
                    Intent gameStartIntent = new Intent(GameCreate.this, ServerLobby.class);
                    Player player = new Player(nickname);
                    ArrayList<Player> playerlist = new ArrayList<>();
                    playerlist.add(player);
                    Lobby lobby = new Lobby(lobbyname, playerlist, randomEvents.isChecked(), chooseMrXRandomly.isChecked(), botMrX.isChecked(), Integer.parseInt(numPlayer));
                    gameStartIntent.putExtra("LOBBY", lobby);
                    Log.d(TAG, "starting ServerLobby");
                    startActivity(gameStartIntent);
                }
            }
        });
    }

    private boolean checkIfValid(String numPlayer, String nickname, String lobbyname, String[] splittedN) {
        if (lobbyname.isEmpty()) {
            lobbyName.setError("Lobbyname darf nicht leer sein!");
            return false;
        }
        if (nickname.isEmpty()) {
            userName.setError("Spielername darf nicht leer sein!");
            return false;
        }
        if (splittedN.length != 1) {
            userName.setError("Spielername darf keine Leerzeichen enthalten!");
            return false;
        }
        if (numPlayer.isEmpty()) {
            maxPlayers.setError("Sie müssen eine Maximalanzahl an Spielern eingeben!");
            return false;
        } else if (Integer.parseInt(numPlayer) < 2 || Integer.parseInt(numPlayer) > 6) {
            maxPlayers.setError("Sie müssen eine gültige Maximalanzahl an Spielern eingeben! (2-6 Spieler möglich)");
            return false;
        }
        return true;
    }
}
