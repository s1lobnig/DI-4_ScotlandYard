package com.example.scotlandyard;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scotlandyard.map.GameMap;

public class GameEnd_Activity extends AppCompatActivity {
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

        sensors();
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
        startActivity(new Intent(GameEnd_Activity.this, MainActivity.class));
    }

    private void sensors() {
        SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(sensorListenerShaking, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        acelVal = SensorManager.GRAVITY_EARTH;
        acelLast = SensorManager.GRAVITY_EARTH;
        shake = 0.00f;

        sm.registerListener(sensorListenerLight, sm.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);

        sm.registerListener(sensorListenerProximity, sm.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);

    }

    private final SensorEventListener sensorListenerShaking = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            acelLast = acelVal;
            acelVal = (float) Math.sqrt((double)(x*x + y*y + z*z));
            float delta = acelVal - acelLast;
            shake = shake + delta;
            //System.out.println(shake);
            if(shake > 7){
                Toast toast = Toast.makeText(getApplicationContext(), "Oh i am turned on <3", Toast.LENGTH_LONG);
                toast.show();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private final SensorEventListener sensorListenerLight = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float curlight = event.values[0];
            //System.out.println(curlight);
            if(curlight < 100){
                Toast toast = Toast.makeText(getApplicationContext(), "Afraid of the darf ?", Toast.LENGTH_LONG);
                toast.show();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private final SensorEventListener sensorListenerProximity = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float distance = event.values[0];
            if(distance < 5){
                Toast toast = Toast.makeText(getApplicationContext(), "#metoo", Toast.LENGTH_LONG);
                toast.show();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

}
