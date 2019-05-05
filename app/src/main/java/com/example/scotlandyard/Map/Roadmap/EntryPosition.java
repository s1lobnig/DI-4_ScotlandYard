package com.example.scotlandyard.Map.Roadmap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.scotlandyard.R;

public class EntryPosition extends Entry {
    private int point;

    public EntryPosition(int turn, int point) {
        super(turn);
        this.point = point;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }


    @Override
    public View getView(Context context) {
        LayoutInflater viewInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View convertView = viewInflater.inflate(R.layout.layout_road_map_entry_position, null);
        ((EditText) convertView.findViewById(R.id.position)).setText((point + 1) + "");
        ((EditText) convertView.findViewById(R.id.turn)).setText(turn + "");
        return convertView;
    }
}