package com.example.scotlandyard.Map.Roadmap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.scotlandyard.R;

public class EntryVehicle extends Entry {
    private int vehicle;
    public EntryVehicle(int turn, int vehicle) {
        super(turn);
    }

    public void setVehicle(int vehicle) {
        this.vehicle = vehicle;
    }

    public int getVehicle() {
        return vehicle;
    }

    @Override
    public View getView(Context context) {
        LayoutInflater viewInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View convertView = viewInflater.inflate(R.layout.layout_road_map_entry_vehicle, null);
        ((ImageView)convertView.findViewById(R.id.vehicleImg)).setImageResource(vehicle);
        ((EditText)convertView.findViewById(R.id.turn)).setText(turn + "");
        return convertView;
    }
}
