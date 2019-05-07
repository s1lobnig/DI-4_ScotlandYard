package com.example.scotlandyard;

import com.example.scotlandyard.Map.Point;
import com.google.android.gms.maps.model.Marker;

import java.io.Serializable;
import java.util.HashMap;

public class Player implements Serializable {
    private String nickname;
    private boolean isActive;
    private int icon;
    private Point position;
    private transient Marker marker;
    private boolean isHost;      //variable for checking if sender is Host
    private HashMap<String, Integer> tickets; //Hashmap for storing tickets
    
    public Player(String nickname) {
        this.nickname = nickname;
        isActive = true;
        this.nickname = nickname;
        this.isHost = false;
        //initialise ticket hashmap for a player with empty tickets
        this.tickets = new HashMap<>();
        this.tickets.put("pedestrian_tickets", 0);
        this.tickets.put("bicycle_tickets", 0);
        this.tickets.put("bus_tickets", 0);
        this.tickets.put("black_tickets", 0);
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
    public void setHost(boolean isHost) {
        this.isHost = isHost;
    }

    public boolean isHost() {
        return isHost;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    @Override
    public String toString() {
        return nickname;
    }
}
