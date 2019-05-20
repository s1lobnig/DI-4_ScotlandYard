package com.example.scotlandyard.map.motions;

import java.io.Serializable;

public class Move implements Serializable {

    private String nickname;
    private int field;
    private int randomEventTrigger;

    public Move(String nickname, int field, int randomEventTrigger) {
        this.nickname = nickname;
        this.field = field;
        this.randomEventTrigger = randomEventTrigger;
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

    public void setRandomEventTrigger(int randomEventTrigger) {
        this.randomEventTrigger = randomEventTrigger;
    }

    public int getRandomEventTrigger() {
        return randomEventTrigger;
    }

    @Override
    public String toString() {
        return "Nickname = " + this.nickname + "; Field = " + this.field;
    }
}
