package com.example.scotlandyard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class GameCreate extends AppCompatActivity {

    private static EditText serverName; /* Server name input field. */
    private static EditText userName; /* User name input field. */
    private static EditText maxPlayers; /* Maximum number of players input field. */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_create);

        serverName = findViewById(R.id.serverName);
        userName = findViewById(R.id.userName);
        maxPlayers = findViewById(R.id.maxPlayers);

        findViewById(R.id.createServerBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Validate maxPlayers input!

                String nameSever = serverName.getText().toString().trim();
                String[] splittedS = nameSever.split(" ");
                String nickname = userName.getText().toString().trim();
                String[] splittedN = nickname.split(" ");

                //nickname must not be empty and must not have any whitespace
                if (nickname.isEmpty() || splittedN.length != 1) {
                    userName.setError("Spielername darf keine Leerzeichen enthalten!");
                }

                //serverName must not be empty and must not have any whitespace
                if (nameSever.isEmpty() || splittedS.length != 1) {
                    serverName.setError("Servername darf keine Leerzeichen enthalten!");
                }

                /* Start game activity. */
                Intent gameStartIntent = new Intent(GameCreate.this, GameActivity.class);
                gameStartIntent.putExtra("ENABLE_BUTTON", true);
                gameStartIntent.putExtra("SERVER_NAME", serverName.getText().toString());
                gameStartIntent.putExtra("USER_NAME", userName.getText().toString());
                gameStartIntent.putExtra("MAX_PLAYERS", Integer.parseInt(maxPlayers.getText().toString()));
                startActivity(gameStartIntent);
            }
        });
    }
}
