package com.example.scotlandyard.EndGame;

import java.io.Serializable;

public class EndGame implements Serializable {
    private boolean hasMrxwon;

    public EndGame(boolean hasMrxwon) {
        this.hasMrxwon = hasMrxwon;
    }

    public boolean isHasMrxwon() {
        return hasMrxwon;
    }

    public void setHasMrxwon(boolean hasMrxwon) {
        this.hasMrxwon = hasMrxwon;
    }
}
