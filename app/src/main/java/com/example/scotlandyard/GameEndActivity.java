package com.example.scotlandyard;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class GameEndActivity extends AppCompatActivity {
    private TextView txtWinners;

    //Variables for shaking
    private float acelVal;  //Curr Value
    private float acelLast; //Last value
    private float shake;    //difference


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_end_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initComp();

    }

    private void initComp() {
        this.txtWinners = findViewById(R.id.txtWinner);

        Intent intent = getIntent();
        boolean winner  = intent.getBooleanExtra("Winner", false);

        if(winner)
            txtWinners.setText("Mr X hat gewonnen");
        else
            txtWinners.setText("Die Detektive haben gewonnen");
    }

    public void openStart(View view) {
        startActivity(new Intent(GameEndActivity.this, MainActivity.class));
    }

}