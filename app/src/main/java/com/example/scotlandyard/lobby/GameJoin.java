package com.example.scotlandyard.lobby;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.scotlandyard.Player;
import com.example.scotlandyard.R;

public class GameJoin extends AppCompatActivity {
    private Button startGame;
    private EditText inputField;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_activty);

        startGame = (Button) findViewById(R.id.btnStartGame);
        inputField = (EditText) findViewById(R.id.editTextInputField);

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = inputField.getText().toString().trim();
                String[] splitted = nickname.split(" ");
                boolean enable = true;

                //nickname must not be empty and must not have any whitespace
                if (nickname.isEmpty()) {
                    inputField.setError("Spielername darf nicht leer sein!");
                    enable = false;
                }
                if (splitted.length != 1) {
                    inputField.setError("Spielername darf keine Leerzeichen enthalten!");
                    enable = false;
                }
                if(nickname == "Bot"){
                    inputField.setError("Spielername darf nicht Bot lauten!");
                    enable = false;
                }
                if (enable) {
                    Intent intent = new Intent(GameJoin.this, GameList.class);
                    Player player = new Player(nickname);
                    intent.putExtra("PLAYER", player);
                    startActivity(intent);
                }
            }
        });

    }
}
