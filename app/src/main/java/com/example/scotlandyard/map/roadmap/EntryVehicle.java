package com.example.scotlandyard.map.roadmap;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.scotlandyard.R;

public class EntryVehicle extends Entry {
    private Ticket ticket;

    public EntryVehicle(int turn, Ticket ticket) {
        super(turn);
        this.ticket = ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public int getTicket() {
        return ticket.getSign();
    }

    @Override
    public View getView(Context context) {
        LayoutInflater viewInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View convertView = viewInflater.inflate(R.layout.layout_road_map_entry_vehicle, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ((ImageView) convertView.findViewById(R.id.vehicleImg)).setImageIcon(Icon.createWithResource(context, getTicket()));
        }
        ((EditText) convertView.findViewById(R.id.turn)).setText(turn + "");
        return convertView;
    }
}
