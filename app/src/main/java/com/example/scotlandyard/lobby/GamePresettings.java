package com.example.scotlandyard.lobby;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.scotlandyard.R;

public class GamePresettings extends AppCompatActivity {

    private static CheckBox randomEvents;
    private static CheckBox chooseMrXRandomly;

    private String serverName;
    private String userName;
    private int maxPlayers;
    private boolean buttonEnabled;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_presettings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        randomEvents = findViewById(R.id.randomEvents);
        chooseMrXRandomly = findViewById(R.id.randomMrX);

        Intent intent = getIntent();
        serverName = intent.getStringExtra("SERVER_NAME");
        userName = intent.getExtras().getString("USER_NAME");
        maxPlayers = intent.getExtras().getInt("MAX_PLAYERS");
        buttonEnabled = intent.getExtras().getBoolean("ENABLE_BUTTON");
        ((Button) findViewById(R.id.proceed)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameStartIntent = new Intent(GamePresettings.this, GameActivity.class);
                gameStartIntent.putExtra("RANDOM_EVENTS", randomEvents.isChecked());
                gameStartIntent.putExtra("RANDOM_MR_X", chooseMrXRandomly.isChecked());
                gameStartIntent.putExtra("ENABLE_BUTTON", buttonEnabled);
                gameStartIntent.putExtra("SERVER_NAME", serverName);
                gameStartIntent.putExtra("USER_NAME", userName);
                gameStartIntent.putExtra("MAX_PLAYERS", maxPlayers);
                startActivity(gameStartIntent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
