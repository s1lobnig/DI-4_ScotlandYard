package com.example.scotlandyard.Map.Roadmap;

public abstract class Entry {
    private int turn;

    public Entry(int turn) {
        this.turn = turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getTurn() {
        return turn;
    }
}
