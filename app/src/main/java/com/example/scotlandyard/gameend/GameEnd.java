package com.example.scotlandyard.gameend;

import java.io.Serializable;

public class GameEnd implements Serializable {
    private boolean hasMrXWon;

    public GameEnd(boolean hasMrXWon) {
        this.hasMrXWon = hasMrXWon;
    }

    public boolean hasMrxwon() {
        return hasMrXWon;
    }

}
