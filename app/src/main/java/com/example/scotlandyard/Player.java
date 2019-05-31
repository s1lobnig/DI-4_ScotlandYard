package com.example.scotlandyard;

import com.example.scotlandyard.lobby.Game;
import com.example.scotlandyard.map.Point;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.gms.maps.model.Marker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player implements Serializable {
    private String nickname;
    private boolean isActive;
    private boolean moved;
    private int icon;
    private Point position;
    private transient Marker marker;
    private boolean isHost;      //variable for checking if sender is Host
    private boolean isMrX;
    private boolean wantsToBeMrX;
    private HashMap<Integer, Integer> tickets; //Hashmap for storing tickets
    private boolean hasCheated;
    private boolean hasCheatedThisRound;

    private int penalty;

    public Player(String nickname) {
        this.nickname = nickname;
        isActive = true;
        moved = false;
        this.nickname = nickname;
        this.isHost = false;
        this.isMrX = false;
        this.wantsToBeMrX = false;
        //initialise ticket hashmap for a player with empty tickets
        this.tickets = new HashMap<>();
        this.tickets.put(R.string.PEDESTRIAN_TICKET_KEY, 0);
        this.tickets.put(R.string.BICYCLE_TICKET_KEY, 0);
        this.tickets.put(R.string.BUS_TICKET_KEY, 0);
        this.tickets.put(R.string.TAXI_TICKET_KEY, 0);
        this.tickets.put(R.string.DOUBLE_TICKET_KEY, 0);
        this.tickets.put(R.string.BLACK_TICKET_KEY, 0);

        this.hasCheated = false;
        this.hasCheatedThisRound = false;
        this.penalty = 0;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public void setHost(boolean isHost) {
        this.isHost = isHost;
    }

    public boolean isHost() {
        return isHost;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public void decreaseNumberOfTickets(Integer key) {
        if (!this.hasCheatedThisRound)
            tickets.put(key, tickets.get(key) - 1);
    }

    public void initializeNumberOfTickets(Object[][] initialTickets) {
        for (int i = 0; i < initialTickets.length; i++) {
            tickets.put((Integer) initialTickets[i][0], (Integer) initialTickets[i][1]);
        }
    }

    public void checkAmountOfTickets() {
        ArrayList<Integer> listOfTickets = new ArrayList<>();

        for (Map.Entry<Integer, Integer> ticket : tickets.entrySet()) {
            // Check if value matches with given value
            if (!ticket.getValue().equals(0)) {
                // Store the key from entry to the list
                listOfTickets.add(ticket.getKey());
            }
        }

        //if all ticket values are 0 set player inactive
        if (listOfTickets.isEmpty()) {
            setActive(false);
        }
    }

    public int[] getRemainingTickets(){
        int[] remainingTickets = new int[tickets.size()];

        for (Map.Entry<Integer, Integer> ticket : tickets.entrySet()) {
            for(int i = 0; i < remainingTickets.length; i++){
                remainingTickets[i] = ticket.getValue();
            }
        }

        return remainingTickets;
    }

    public boolean isMrX() {
        return isMrX;
    }

    public void setMrX(boolean mrX) {
        isMrX = mrX;
    }

    public void setWantsToBeMrX(boolean wantsToBeMrX) {
        this.wantsToBeMrX = wantsToBeMrX;
    }

    public boolean wantsToBeMrX() {
        return wantsToBeMrX;
    }

    public HashMap<Integer, Integer> getTickets() {
        return tickets;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    public int getPenalty() {
        return penalty;
    }

    public void decreasePenalty() {
        penalty--;
    }

    @Override
    public String toString() {
        return nickname;
    }


    public boolean isHasCheated() {
        return hasCheated;
    }

    public void setHasCheated(boolean hasCheated) {
        this.hasCheated = hasCheated;
    }

    public boolean isHasCheatedThisRound() {
        return hasCheatedThisRound;
    }

    public void setHasCheatedThisRound(boolean hasCheatedThisRound) {
        this.hasCheatedThisRound = hasCheatedThisRound;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Player) {
            Player p = (Player) o;
            if (p.nickname.equals(nickname)) {
                return true;
            }
        }
        return false;
    }

}
