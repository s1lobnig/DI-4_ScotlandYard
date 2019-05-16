package com.example.scotlandyard.map.motions;

import java.io.Serializable;

public class Move implements Serializable {
    private String nickname;
    private int field;

    public Move(String nickname, int field) {
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

    @Override
    public String toString() {
        return "Nickname = " + this.nickname + "; Field = " + this.field;
    }
}
