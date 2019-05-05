package com.example.scotlandyard.Map.Roadmap;

public class EntryPosition extends Entry {
    private int point;

    public EntryPosition(int turn, int point) {
        super(turn);
        this.point = point;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
