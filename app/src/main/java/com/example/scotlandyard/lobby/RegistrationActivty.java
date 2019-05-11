package com.example.scotlandyard.lobby;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.scotlandyard.R;

public class RegistrationActivty extends AppCompatActivity {

    public static final String passNickname = "com.example.scotlandyard.java.passNickname";

    private Button startGame;
    private EditText inputField;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_activty);

        startGame = (Button) findViewById(R.id.btnStartGame);
        inputField = (EditText) findViewById(R.id.editTextInputField);

        inputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = inputField.getText().toString().trim();
                String[] splitted = nickname.split(" ");

                //nickname must not be empty and must not have any whitespace
                if (nickname.isEmpty() || splitted.length != 1) {
                    inputField.setError("Spielername darf keine Leerzeichen enthalten!");
                } else {
                    try {
                        openGame();
                    } catch (InterruptedException e) {
                        Log.e("InterruptedExeption", e.getMessage(), e.getCause());
                    }
                }
            }
        });

    }

    public void openGame() throws InterruptedException {
        String nickname = inputField.getText().toString().trim();

        if (!nickname.isEmpty()) {
            Intent intent = new Intent(RegistrationActivty.this, GameList.class);
            intent.putExtra("USERNAME", nickname);
            startActivity(intent);
            // Simulate waiting time to join game
            Thread.sleep(2000);
        }
    }
}
