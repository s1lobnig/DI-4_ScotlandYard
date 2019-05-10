package com.example.scotlandyard.map.roadmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.LinearLayout;

import com.example.scotlandyard.R;

public class RoadMapDisplay extends AppCompatActivity {

    private RoadMap roadMap;

    private static LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_map_display);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        roadMap = (RoadMap) intent.getSerializableExtra("ROAD_MAP");

        linearLayout = findViewById(R.id.road_map_list);
        for (Entry e : roadMap.getEntries()) {
            Log.d("VIEW", e.getView(this).toString());
            linearLayout.addView(e.getView(this));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
