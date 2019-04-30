package com.example.scotlandyard;

import android.os.Parcelable;

import com.google.android.gms.maps.model.Marker;

import java.io.Serializable;

public class Player implements Serializable {
    private String nickname;
    private int icon;
    private Marker marker;
    private boolean isHost;      //variable for checking if sender is Host

    public Player(String nickname) {
        this.nickname = nickname;
        this.isHost = false;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    public void setHost(boolean isHost) {
        this.isHost = isHost;
    }

    public boolean isHost() {
        return isHost;

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
