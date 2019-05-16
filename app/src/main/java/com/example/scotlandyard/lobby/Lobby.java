package com.example.scotlandyard.lobby;

import com.example.scotlandyard.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class Lobby implements Serializable {
    private String lobbyName;
    private ArrayList<Player> playerList;
    private boolean randomEvents;
    private boolean randomMrX;
    private int maxPlayers;

    public Lobby(String lobbyName, Player player, boolean randomEvents, boolean randomMrX, int maxPlayers) {

    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getPlayerCount() {
        return playerList.size();
    }

    public void addPlayer(Player player) {
        playerList.add(player);
    }

    public void removePlayer(String playerName) {
        for (Player p : playerList) {
            if (p.getNickname().equals(playerName)) {
                playerList.remove(p);
                break;
            }
        }
    }
}
