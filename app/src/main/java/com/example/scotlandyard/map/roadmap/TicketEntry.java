package com.example.scotlandyard.map.roadmap;

import android.view.View;

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
    public View getView() {
        return null;
    }
}
