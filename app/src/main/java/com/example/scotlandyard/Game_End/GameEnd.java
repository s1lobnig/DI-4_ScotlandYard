package com.example.scotlandyard.Game_End;

import java.io.Serializable;

public class GameEnd implements Serializable {
    private boolean hasMrxwon;

    public GameEnd(boolean hasMrxwon) {
        this.hasMrxwon = hasMrxwon;
    }

    public boolean HasMrxwon() {
        return hasMrxwon;
    }

    public void setHasMrxwon(boolean hasMrxwon) {
        this.hasMrxwon = hasMrxwon;
    }

}
