package com.example.scotlandyard.map.motions;

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