package com.example.scotlandyard.lobby;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.scotlandyard.R;

public class GameCreate extends AppCompatActivity {

    private static EditText userName; /* User name input field. */
    private static EditText maxPlayers; /* Maximum number of players input field. */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_create);

        userName = findViewById(R.id.userName);
        maxPlayers = findViewById(R.id.maxPlayers);

        findViewById(R.id.createServerBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Validate maxPlayers input!

                String numPlayer = maxPlayers.getText().toString();

                String nickname = userName.getText().toString().trim();
                String[] splittedN = nickname.split(" ");
                boolean enable = true;

                //nickname must not be empty and must not have any whitespace
                if (nickname.isEmpty() || splittedN.length != 1) {
                    userName.setError("Spielername darf keine Leerzeichen enthalten!");
                    enable = false;
                }

                if(numPlayer == null || numPlayer.equals("")){
                    maxPlayers.setError("Sie müssen eine Maximalanzahl an Spielern eingeben!");
                    enable = false;
                }

                if(enable) {
                    /* Start game activity. */
                    Intent gameStartIntent = new Intent(GameCreate.this, GameActivity.class);
                    gameStartIntent.putExtra("ENABLE_BUTTON", true);
                    gameStartIntent.putExtra("USER_NAME", userName.getText().toString());
                    gameStartIntent.putExtra("MAX_PLAYERS", Integer.parseInt(maxPlayers.getText().toString()));
                    startActivity(gameStartIntent);
                }
            }
        });
    }
}
