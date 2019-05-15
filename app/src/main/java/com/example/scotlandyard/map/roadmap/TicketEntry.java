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

public class TicketEntry extends Entry {
    private Ticket ticketUsed;

    public TicketEntry(int turnNumber, int ticket) {
        super(turnNumber);
        this.ticketUsed = Ticket.get(ticket);
    }

    public void setTicketUsed(int ticket) {
        this.ticketUsed = Ticket.get(ticket);
    }

    public Ticket getTicketUsed() {
        return ticketUsed;
    }

    @Override
    public View getView(Context context) {
        LayoutInflater viewInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View convertView = viewInflater.inflate(R.layout.layout_road_map_entry_ticket, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ((ImageView) convertView.findViewById(R.id.vehicleImg)).setImageIcon(Icon.createWithResource(context, ticketUsed.getSign()));
        }
        ((EditText) convertView.findViewById(R.id.turn)).setText(turnNumber + "");
        return convertView;
    }
}
