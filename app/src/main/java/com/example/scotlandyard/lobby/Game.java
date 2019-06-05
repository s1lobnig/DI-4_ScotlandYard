package com.example.scotlandyard.lobby;

import com.example.scotlandyard.Player;
import com.example.scotlandyard.R;
import com.example.scotlandyard.control.Device;
import com.example.scotlandyard.map.ManageGameData;
import com.example.scotlandyard.map.Point;
import com.example.scotlandyard.map.Points;
import com.example.scotlandyard.map.motions.Move;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Game implements Serializable {

    private static final Random random = new Random();
    private static final int numRounds = 12;
    private static final int[] PLAYER_ICONS = {
            R.drawable.player1,
            R.drawable.player2,
            R.drawable.player3,
            R.drawable.player4,
            R.drawable.player5,
            R.drawable.player6,
            R.drawable.player7,
            R.drawable.player8,
            R.drawable.player9,
            R.drawable.player10,
            R.drawable.player11
    };

    private String gameName;
    private int maxMembers;
    private int currentMembers;
    private int round;
    private boolean roundMrX;
    private boolean randomEventsEnabled;
    private boolean botMrX;
    private ArrayList<Player> players = new ArrayList<>(); // Changed from List to ArrayList because of serialization.


    public Game(String gameName, int maxMembers) {
        this.gameName = gameName;
        this.maxMembers = maxMembers;
        this.currentMembers = 1;
    }

    public Game(String gameName, int maxMembers, int currentMembers, int round, boolean randomEventsEnabled, boolean botMrX, ArrayList<Player> players) {
        this.gameName = gameName;
        this.maxMembers = maxMembers;
        this.currentMembers = currentMembers;
        this.round = round;
        this.roundMrX = true;
        this.randomEventsEnabled = randomEventsEnabled;
        this.botMrX = botMrX;
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

    public boolean isBotMrX() {
        return botMrX;
    }

    public void setBotMrX(boolean botMrX) {
        this.botMrX = botMrX;
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

    public Player getBotMrX() {
        if(botMrX) {
            return getMrX();
        }
        return null;
    }

    public Player findPlayer(String nickname) {
        for (Player p : players) {
            if (p.getNickname().equals(nickname)) {
                return p;
            }
        }
        return null;
    }

    private boolean isRoundFinished() {
        for (Player p : players) {
            if (p.isActive() && !p.isMoved()) {
                return false;
            }
        }
        return true;
    }

    public int deactivatePlayer(Player player){
        if(player.isMrX()){
            setBotMrX(true);
        }
        player.setActive(false);
        return  tryNextRound();
    }

    public int tryNextRound() {
        if (isRoundMrX()) {
            if (getMrX().isMoved()) {
                setRoundMrX(false);
                //return -1;
            }
        }
        if (isRoundFinished()) {
            if (round < numRounds) {
                /*if(this.isMrXFound()){
                    return 2;
                }*/
                //Round finished
                round++;
                setRoundMrX(true);
                for (Player p : players) {
                    p.setMoved(false);
                }
                return 1;
            }
            //Game finished
            round++;
            return 0;
        }
        //Round not finished yet
        return -1;
    }

    public boolean isPlayer(Marker field) {
        for (Player player : players) {
            if (player.getMarker().equals(field)) {
                return true;
            }
        }
        return false;
    }

    public void givePlayerPositionAndIcon() {
        Player player;
        for (int i = 0; i < players.size(); i++) {
            player = players.get(i);
            player.setIcon(PLAYER_ICONS[i]);
            LatLng position = getNewPlayerPosition();
            player.setPosition(new Point(position.latitude, position.longitude));
            player.setMoved(false);

            //setTickets for every player
            player.setTickets(this, player);
        }
    }

    //returns a free position
    private LatLng getNewPlayerPosition() {
        int position = (new Random()).nextInt(Points.getPoints().length);
        Point point = Points.POINTS[position];
        for (Player p : players) {
            if (p.getPosition() == point) {
                return getNewPlayerPosition();
            }
        }
        return point.getLatLng();
    }

    /*private boolean isMrXFound(){
        Player x = this.getMrX();
        for(Player p : game.getPlayers()){
            if (p.getPosition() == x.getPosition() && p.getNickname() != x.getNickname())
                return true;
        }
        return false;
    }*/
}
