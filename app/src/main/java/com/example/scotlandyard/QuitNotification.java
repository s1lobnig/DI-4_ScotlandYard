package com.example.scotlandyard;

import java.io.Serializable;

public class QuitNotification implements Serializable {
    private String playerName;
    private boolean serverQuit;

    public QuitNotification(String playerName, boolean serverQuit) {
        this.playerName = playerName;
        this.serverQuit = serverQuit;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public boolean isServerQuit() {
        return serverQuit;
    }

    public void setServerQuit(boolean serverQuit) {
        this.serverQuit = serverQuit;
    }
}
