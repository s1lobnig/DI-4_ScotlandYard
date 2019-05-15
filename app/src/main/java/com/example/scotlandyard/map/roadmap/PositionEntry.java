package com.example.scotlandyard.map.roadmap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.scotlandyard.R;

public class PositionEntry extends Entry {

    private int position;

    public PositionEntry(int turnNumber, int position) {
        super(turnNumber);
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public View getView(Context context) {
        LayoutInflater viewInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View convertView = viewInflater.inflate(R.layout.layout_road_map_entry_position, null);
        ((EditText) convertView.findViewById(R.id.position)).setText((position + 1) + "");
        ((EditText) convertView.findViewById(R.id.turn)).setText(turnNumber + "");
        return convertView;
    }
}
