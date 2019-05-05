package com.example.scotlandyard.Map.Motions;

import com.example.scotlandyard.Player;
import com.google.android.gms.maps.model.Marker;

import java.io.Serializable;

public class SendMove implements Serializable {
    private String nickname;
    private int field;

    public SendMove(String nickname, int field) {
        this.nickname = nickname;
        this.field = field;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getField() {
        return field;
    }

    public void setField(int field) {
        this.field = field;
    }
}
