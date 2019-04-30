package com.example.scotlandyard;

import com.google.android.gms.maps.model.Marker;

import java.io.Serializable;

public class Player implements Serializable {
    private String nickname;
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
    }

    @Override
    public String toString() {
        return nickname;
    }
}
