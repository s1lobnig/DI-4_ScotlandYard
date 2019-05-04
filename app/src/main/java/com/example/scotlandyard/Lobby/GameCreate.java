package com.example.scotlandyard.Lobby;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.scotlandyard.R;

public class GameCreate extends AppCompatActivity {

    private static EditText serverName; /* Server name input field. */
    private static EditText userName; /* User name input field. */
    private static EditText maxPlayers; /* Maximum number of players input field. */
    private static CheckBox randomEvents;
    private static CheckBox chooseMrXRandomly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_create);

        serverName = findViewById(R.id.serverName);
        userName = findViewById(R.id.userName);
        maxPlayers = findViewById(R.id.maxPlayers);
        randomEvents = findViewById(R.id.randomEvents);
        chooseMrXRandomly = findViewById(R.id.randomMrX);

        findViewById(R.id.createServerBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Validate serverName and maxPlayers input like in registration class!

                /* Start game activity. */
                Intent gameStartIntent = new Intent(GameCreate.this, GameActivity.class);
                gameStartIntent.putExtra("ENABLE_BUTTON", true);
                gameStartIntent.putExtra("SERVER_NAME", serverName.getText().toString());
                gameStartIntent.putExtra("USER_NAME", userName.getText().toString());
                gameStartIntent.putExtra("MAX_PLAYERS", Integer.parseInt(maxPlayers.getText().toString()));
                gameStartIntent.putExtra("RANDOM_EVENTS", randomEvents.isChecked());
                gameStartIntent.putExtra("RANDOM_MR_X", chooseMrXRandomly.isChecked());
                startActivity(gameStartIntent);
            }
        });
    }
}
