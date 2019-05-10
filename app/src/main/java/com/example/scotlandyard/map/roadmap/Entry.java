package com.example.scotlandyard.map.roadmap;

import android.content.Context;
import android.view.View;

import java.io.Serializable;

public abstract class Entry implements Serializable {
    protected int turn;

    public Entry(int turn) {
        this.turn = turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getTurn() {
        return turn;
    }

    public abstract View getView(Context context);
}
