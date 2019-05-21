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
        this.randomEventsEnabled = randomEventsEnabled;
        this.players = players;
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

}
