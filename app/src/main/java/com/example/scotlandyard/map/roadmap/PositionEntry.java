package com.example.scotlandyard.map.roadmap;

import android.view.View;

public class PositionEntry extends Entry {

    private int position;

    public PositionEntry(int turnNumber, int position) {
        super(turnNumber);
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public View getView() {
        return null;
    }
}
