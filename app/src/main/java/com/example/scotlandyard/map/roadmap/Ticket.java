package com.example.scotlandyard.map.roadmap;

import com.example.scotlandyard.R;

public enum Ticket {

    FOOT(R.drawable.ticket_yellow), BICYCLE(R.drawable.ticket_orange),
    BUS(R.drawable.ticket_red), TAXI_DRAGAN(R.drawable.ticket_blue),
    BLACK(R.drawable.ticket_black), DOUBLE(R.drawable.ticket_green);

    private final int sign;

    Ticket(int sign) {
        this.sign = sign;
    }

    public static Ticket get(int sign) {
        switch (sign) {
            case R.drawable.ticket_yellow:
                return FOOT;
            case R.drawable.ticket_orange:
                return BICYCLE;
            case R.drawable.ticket_red:
                return BUS;
            case R.drawable.ticket_blue:
                return TAXI_DRAGAN;
            case R.drawable.ticket_black:
                return BLACK;
            case R.drawable.ticket_green:
                return DOUBLE;
            default:
                return null;
        }
    }

    public int getSign() {
        return sign;
    }
}
