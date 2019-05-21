package com.example.scotlandyard.lobby;

import com.example.scotlandyard.Player;
import com.example.scotlandyard.map.motions.Move;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Game implements Serializable {

    private static final Random random = new Random();
    private static final int numRounds = 12;

    private String gameName;
    private int maxMembers;
    private int currentMembers;
    private int round;
    private boolean roundMrX;
    private boolean randomEventsEnabled;
    private ArrayList<Player> players = new ArrayList<>(); // Changed from List to ArrayList because of serialization.


    public Game(String gameName, int maxMembers) {
        this.gameName = gameName;
        this.maxMembers = maxMembers;
        this.currentMembers = 1;
    }

    public Game(String gameName, int maxMembers, int currentMembers, int round, boolean randomEventsEnabled, ArrayList<Player> players) {
        this.gameName = gameName;
        this.maxMembers = maxMembers;
        this.currentMembers = currentMembers;
        this.round = round;
        this.roundMrX = true;
        this.randomEventsEnabled = randomEventsEnabled;
        this.players = players;
    }

    public static int getNumRounds() {
        return numRounds;
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

    public void nextRound() {
        this.round++;
    }

    public boolean isRoundMrX() {
        return roundMrX;
    }

    public void setRoundMrX(boolean roundMrX) {
        this.roundMrX = roundMrX;
    }

    public boolean isRandomEventsEnabled() {
        return randomEventsEnabled;
    }

    public void setRandomEventsEnabled(boolean randomEventsEnabled) {
        this.randomEventsEnabled = randomEventsEnabled;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public Player getMrX(){
        for (Player p : players) {
            if(p.isMrX()){
                return p;
            }
        }
        return null;
    }
}
