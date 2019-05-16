package com.example.scotlandyard.lobby;

import com.example.scotlandyard.Player;
import com.example.scotlandyard.map.motions.Move;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Game implements Serializable {

    private static final Random random = new Random();
    private String gameName;
    private int maxMembers;
    private int currentMembers;
    private int round;
    private ArrayList<Player> players = new ArrayList<>(); // Changed from List to ArrayList because of serialization.


    public Game(String gameName, int maxMembers) {
        this.gameName = gameName;
        this.maxMembers = maxMembers;
        this.currentMembers = 1;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int getMaxMembers() {
        return maxMembers;
    }

    public void setMaxMembers(int maxMembers) {
        this.maxMembers = maxMembers;
    }

    public int getCurrentMembers() {
        return currentMembers;
    }

    public void setCurrentMembers(int currentMembers) {
        this.currentMembers = currentMembers;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public void nextRound(){
        this.round++;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public boolean nickAlreadyUsed(String nick) {
        for (Player p : players) {
            if (nick.equals(p.getNickname())) {
                return true;
            }
        }
        return false;
    }

    public boolean doMove(Move move) {
        //TODO check and make the move
        return false;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void chooseMrX(boolean chooseMrXRandomly) {
        if (chooseMrXRandomly) {
            players.get(random.nextInt(players.size())).setMrX(true);
        } else {
            ArrayList<Integer> candidatesForMrX = new ArrayList<>();
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).wantsToBeMrX())
                    candidatesForMrX.add(i);
            }
            if (candidatesForMrX.isEmpty())
                players.get(0).setMrX(true);
            else
                players.get(candidatesForMrX.get(random.nextInt(candidatesForMrX.size()))).setMrX(true);
        }
    }
}
