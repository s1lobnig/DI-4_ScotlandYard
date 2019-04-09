package com.example.scotlandyard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivty extends AppCompatActivity {

    public static final String passNickname = "com.example.scotlandyard.java.passNickname";

    private Button startGame;
    private EditText inputField;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_activty);

        startGame = (Button) findViewById(R.id.buttonStartGame);
        inputField = (EditText) findViewById(R.id.editTextInputField);

        inputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String nickname = inputField.getText().toString().trim();
                startGame.setEnabled(!nickname.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {
                String nickname = inputField.getText().toString().trim();
                startGame.setEnabled(!nickname.isEmpty());

            }
        });

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = inputField.getText().toString().trim();
                String message = "Sie können das Spiel jetzt beitreten!";

                if (nickname.isEmpty()) {
                    inputField.setError("Sie müssen sich für einen Spielername entscheiden!");
                } else {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
                try {
                    openGame();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void openGame() throws InterruptedException {
        String nickname = inputField.getText().toString().trim();

        if (!nickname.isEmpty()) {
            Intent intent = new Intent(RegistrationActivty.this, GameActivity.class);
            intent.putExtra(passNickname, nickname);
            startActivity(intent);
            Thread.sleep(2000);
        }
    }
}