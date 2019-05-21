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
        ImageView ticketImage = (ImageView) convertView.findViewById(R.id.vehicleImg);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ticketImage.setImageIcon(Icon.createWithResource(context, ticketUsed.getSign()));
        } else {
            ticketImage.setImageResource(ticketUsed.getSign());
        }
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
            if (o instanceof TicketEntry) {
                TicketEntry ticketEntry = (TicketEntry) o;
                if (ticketEntry.ticketUsed == ticketUsed && ticketEntry.turnNumber == turnNumber)
                    return true;
            }
            return false;
        }
        return false;
    }
}
