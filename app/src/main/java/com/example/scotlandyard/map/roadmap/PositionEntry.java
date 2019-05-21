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
        this.position = position - 1;
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
        ((EditText) convertView.findViewById(R.id.position)).setText((position) + "");
        ((EditText) convertView.findViewById(R.id.turn)).setText(turnNumber + "");
        return convertView;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Entry) {
            if (o instanceof PositionEntry) {
                PositionEntry positionEntry = (PositionEntry) o;
                if (positionEntry.position == position && positionEntry.turnNumber == turnNumber)
                    return true;
            }
            return false;
        }
        return false;
    }
}
