package com.example.scotlandyard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import static com.example.scotlandyard.R.id.textViewNickname;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        String getNickname = intent.getStringExtra(RegistrationActivty.passNickname);

        TextView textView = (TextView) findViewById(textViewNickname);
        textView.setText(getNickname);
    }
}
