package com.example.scotlandyard;

import com.google.android.gms.maps.model.Marker;

import java.io.Serializable;

public class Player implements Serializable {
    private String nickname;
    private boolean isActive;
    private int icon;
    private Marker marker;

    public Player(String nickname) {
        this.nickname = nickname;
        isActive = true;
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

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
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
