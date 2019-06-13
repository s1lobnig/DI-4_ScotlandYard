package com.example.scotlandyard.gameend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.scotlandyard.MainActivity;
import com.example.scotlandyard.QuitNotification;
import com.example.scotlandyard.R;
import com.example.scotlandyard.control.Device;
import com.example.scotlandyard.control.Server;

public class GameEndActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_end_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initComp();
    }

    private void initComp() {
        TextView txtWinners = findViewById(R.id.txtWinner);
        Intent intent = getIntent();
        boolean winner = intent.getBooleanExtra("Winner", false);
        if (winner)
            txtWinners.setText("Mr X hat gewonnen");
        else
            txtWinners.setText("Die Detektive haben gewonnen");
    }

    public void openStart(View view) {
       if (Device.isServer()) {
           Device device = Device.getInstance();
           device.send(new QuitNotification(device.getNickname(), true));
           ((Server)device).disconnect();
       }
       startActivity(new Intent(GameEndActivity.this, MainActivity.class));
    }

}
