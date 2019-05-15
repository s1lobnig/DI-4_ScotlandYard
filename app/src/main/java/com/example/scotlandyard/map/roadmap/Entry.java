package com.example.scotlandyard.map.roadmap;

import android.view.View;

public abstract class Entry {
    protected int turnNumber;

    public Entry(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public abstract View getView();

}
