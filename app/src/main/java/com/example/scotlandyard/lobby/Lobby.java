package com.example.scotlandyard.lobby;

import com.example.scotlandyard.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class Lobby implements Serializable {
    private String lobbyName;
    private ArrayList<Player> players;
    private boolean randomEvents;
    private boolean randomMrX;
    private int maxPlayers;

    public Lobby(String lobbyName, ArrayList players, boolean randomEvents, boolean randomMrX, int maxPlayers) {

    }

}
