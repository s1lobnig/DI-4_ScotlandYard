package com.example.scotlandyard;

import com.example.scotlandyard.control.Device;
import com.example.scotlandyard.lobby.Game;
import com.example.scotlandyard.map.Point;
import com.example.scotlandyard.map.Points;
import com.example.scotlandyard.map.Routes;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.gms.maps.model.LatLng;
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

    public static void setTickets(Game game, Player player) {
        HashMap<Integer, Integer> tickets;
        //set Ticket for Mr. X
        if (player.isMrX()) {
            tickets = player.getTickets();
            tickets.put(R.string.PEDESTRIAN_TICKET_KEY, Integer.MAX_VALUE);
            tickets.put(R.string.BICYCLE_TICKET_KEY, Integer.MAX_VALUE);
            tickets.put(R.string.BUS_TICKET_KEY, Integer.MAX_VALUE);
            tickets.put(R.string.TAXI_TICKET_KEY, 2);
            tickets.put(R.string.DOUBLE_TICKET_KEY, 1);
            tickets.put(R.string.BLACK_TICKET_KEY, game.getPlayers().size() - 1);

        } else {
            //set Tickets for all other players
            tickets = player.getTickets();
            tickets.put(R.string.PEDESTRIAN_TICKET_KEY, 5);
            tickets.put(R.string.BICYCLE_TICKET_KEY, 4);
            tickets.put(R.string.BUS_TICKET_KEY, 2);
            tickets.put(R.string.TAXI_TICKET_KEY, 0);
            tickets.put(R.string.DOUBLE_TICKET_KEY, 0);
            tickets.put(R.string.BLACK_TICKET_KEY, 0);
        }

    }

    public void decreaseNumberOfTickets(Integer key) {
        if (!this.hasCheatedThisRound)
            tickets.put(key, tickets.get(key) - 1);
    }

    public static boolean checkForValidTicket(Player player, int vehicle) {
        boolean validTicket;
        HashMap<Integer, Integer> tickets = player.getTickets();
        if (vehicle == 0 && tickets.get(R.string.PEDESTRIAN_TICKET_KEY) > 0) {
            validTicket = true;
        } else if (vehicle == 1 && tickets.get(R.string.BICYCLE_TICKET_KEY) > 0) {
            validTicket = true;
        } else if (vehicle == 2 && tickets.get(R.string.BUS_TICKET_KEY) > 0) {
            validTicket = true;
        } else if (vehicle == 3 && tickets.get(R.string.TAXI_TICKET_KEY) > 0) {
            validTicket = true;
        } else if (vehicle == 4 && tickets.get(R.string.DOUBLE_TICKET_KEY) > 0) {
            validTicket = true;
        } else if (vehicle == 5 && tickets.get(R.string.BLACK_TICKET_KEY) > 0) {
            validTicket = true;
        } else {
            validTicket = false;
        }
        return validTicket;
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

    public int[] getRemainingTickets() {

        int[] remainingTickets = {tickets.get(R.string.PEDESTRIAN_TICKET_KEY).intValue(),
                tickets.get(R.string.BICYCLE_TICKET_KEY).intValue(),
                tickets.get(R.string.BUS_TICKET_KEY).intValue(),
                tickets.get(R.string.TAXI_TICKET_KEY).intValue(),
                tickets.get(R.string.DOUBLE_TICKET_KEY).intValue(),
                tickets.get(R.string.BLACK_TICKET_KEY).intValue()};

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

    /**
     * checks if a chosen route is valid for the player
     *
     * @param destination position, where player wants to go
     * @return 0 if valid
     * 1 if no move or game has end (invalid)
     * 2 if player is not active (invalid)
     * 3 if not players turn (invalid)
     * 4 if bicicle not availabe (invalid)
     * 5 if not enought tickets (invalid)
     * 6 if position not reachable (invalid)
     */
    public int isValidMove(Marker destination) {
        if (Device.getInstance().getGame().isPlayer(destination) || Device.getInstance().getGame().getRound() > Game.getNumRounds()) {
            return 1;
        }
        if(!isActive){
            return 2;
        }
        //if it is not players turn -> ignore move
        if (moved || (isMrX && !Device.getInstance().getGame().isRoundMrX()) || (!isMrX && Device.getInstance().getGame().isRoundMrX())) {
            return 3;
        }
        LatLng current = marker.getPosition();
        Point currentPoint = new Point(current.latitude, current.longitude);
        Point newLocation = new Point(destination.getPosition().latitude, destination.getPosition().longitude);
        Object[] routeToTake = Routes.getRoute(Points.getIndex(currentPoint), Points.getIndex(newLocation));

        if ((Boolean) routeToTake[0]) {
            // if player has penalty and wants to take the bicycle
            if (penalty > 0 && (int) (routeToTake[2]) == 1) {
                return 4;
            }
            boolean enoughTickets = checkForValidTicket(this, (int) routeToTake[2]);
            if (!enoughTickets) {
                return 5;
            }

            return 0;
        }
        return 6;
    }
}
