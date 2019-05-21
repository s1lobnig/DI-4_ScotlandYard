package com.example.scotlandyard.lobby;

import com.example.scotlandyard.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Lobby implements Serializable {
    private String lobbyName;
    private ArrayList<Player> playerList;
    private boolean randomEvents;
    private boolean randomMrX;
    private int maxPlayers;

    public Lobby(String lobbyName, ArrayList<Player> playerList, boolean randomEvents, boolean randomMrX, int maxPlayers) {
        this.lobbyName = lobbyName;
        this.playerList = playerList;
        this.randomEvents = randomEvents;
        this.randomMrX = randomMrX;
        this.maxPlayers = maxPlayers;
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

    public boolean isRandomEvents() {
        return randomEvents;
    }

    public boolean isRandomMrX() {
        return randomMrX;
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

    public boolean nickAlreadyUsed(String nick) {
        for (Player p : playerList) {
            if (nick.equals(p.getNickname())) {
                return true;
            }
        }
        return false;
    }

    public void chooseMrX(boolean chooseMrXRandomly) {
        Random random = new Random();
        if (chooseMrXRandomly) {
            playerList.get(random.nextInt(playerList.size())).setMrX(true);
            return;
        } else {
            ArrayList<Integer> candidatesForMrX = new ArrayList<>();
            for (int i = 0; i < playerList.size(); i++) {
                if (playerList.get(i).wantsToBeMrX())
                    candidatesForMrX.add(i);
            }
            if (candidatesForMrX.isEmpty()) {
                playerList.get(0).setMrX(true);
                return;
            } else
                playerList.get(candidatesForMrX.get(random.nextInt(candidatesForMrX.size()))).setMrX(true);
        }
    }
}
