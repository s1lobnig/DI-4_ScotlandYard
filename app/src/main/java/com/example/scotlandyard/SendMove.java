package com.example.scotlandyard;

import com.google.android.gms.maps.model.Marker;

import java.io.Serializable;

public class SendMove implements Serializable {
    private String nickname;
    private Marker field;

    public SendMove(String nickname, Marker field) {
        this.nickname = nickname;
        this.field = field;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Marker getField() {
        return field;
    }

    public void setField(Marker field) {
        this.field = field;
    }
}
