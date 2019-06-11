package com.example.scotlandyard.map.motions;

import com.example.scotlandyard.map.ValidatedRoute;

import java.io.Serializable;

public class Move implements Serializable {

    private String nickname;
    private int field;
    private int randomEventTrigger;
    private ValidatedRoute randomRoute;
    private boolean cheatingMove;

    public Move(String nickname, int field, int randomEventTrigger, ValidatedRoute randomRoute) {
        this.nickname = nickname;
        this.field = field;
        this.randomEventTrigger = randomEventTrigger;
        this.randomRoute = randomRoute;
        this.cheatingMove = false;
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

    public int getRandomEventTrigger() {
        return randomEventTrigger;
    }

    public ValidatedRoute getRandomRoute() {
        return randomRoute;
    }

    @Override
    public String toString() {
        return "Nickname = " + this.nickname + "; Field = " + this.field;
    }

    public boolean isCheatingMove() {
        return cheatingMove;
    }

    public void setCheatingMove(boolean cheatingMove) {
        this.cheatingMove = cheatingMove;
    }
}
