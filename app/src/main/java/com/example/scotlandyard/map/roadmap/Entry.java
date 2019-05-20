package com.example.scotlandyard.map.roadmap;

import android.content.Context;
import android.view.View;

import java.io.Serializable;

public abstract class Entry implements Serializable {
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

    public abstract View getView(Context context);

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
