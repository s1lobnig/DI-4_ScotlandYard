package com.example.scotlandyard.map.roadmap;

import com.example.scotlandyard.R;

public enum Ticket {

    FOOT(R.drawable.ticket_yellow),BICYCLE(R.drawable.ticket_orange),BUS(R.drawable.ticket_red),TAXI_DRAGAN(R.drawable.ticket_black);

    private final int sign;
    Ticket(int sign){
        this.sign = sign;
    }

    public static Ticket get(int sign){
        switch(sign){
            case R.drawable.ticket_yellow:
                return FOOT;
            case R.drawable.ticket_orange:
                return BICYCLE;
            case R.drawable.ticket_red:
                return BUS;
            case R.drawable.ticket_black:
                return TAXI_DRAGAN;
            default:
                return null;
        }
    }

    public int getSign() {
        return sign;
    }
}
