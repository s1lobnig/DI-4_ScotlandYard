package com.example.scotlandyard;

import com.example.scotlandyard.map.Point;
import com.example.scotlandyard.map.Points;
import com.example.scotlandyard.map.Routes;
import com.example.scotlandyard.map.ValidatedRoute;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player implements Serializable {
    private String nickname;
    private boolean isActive;
    private boolean moved;
    private int icon;
    private Point position;
    private transient Marker marker;
    private boolean isMrX;
    private boolean wantsToBeMrX;
    private HashMap<Integer, Integer> tickets; //Hashmap for storing tickets
    private boolean hasCheated;
    private boolean hasCheatedThisRound;
    private boolean[] specialMrXMoves;
    private boolean hascheated;
    private int countCheatingmoves;
    private int penalty;

    public Player(String nickname) {
        this.nickname = nickname;
        this.isActive = true;
        this.moved = false;
        this.isMrX = false;
        this.wantsToBeMrX = false;
        this.hasCheated = false;
        this.countCheatingmoves = 0;
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
        this.specialMrXMoves = new boolean[]{false, false};
    }

    public void setSpecialMrXMoves(boolean[] specialMrXMoves) {
        if (isMrX) {
            this.specialMrXMoves = specialMrXMoves;
        }
    }

    public void setSpecialMrXMoves(boolean specialMrXMove, int i) {
        if (isMrX) {
            this.specialMrXMoves[i] = specialMrXMove;
        }
    }


    public boolean[] getSpecialMrXMoves() {
        return specialMrXMoves;
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
        /*if(isMrX)
            marker.setVisible(false);*/
    }

    public void setTickets(Game game) {
        //set Ticket for Mr. X
        if (isMrX) {
            tickets.put(R.string.PEDESTRIAN_TICKET_KEY, Integer.MAX_VALUE);
            tickets.put(R.string.BICYCLE_TICKET_KEY, Integer.MAX_VALUE);
            tickets.put(R.string.BUS_TICKET_KEY, Integer.MAX_VALUE);
            tickets.put(R.string.TAXI_TICKET_KEY, 2);
            tickets.put(R.string.DOUBLE_TICKET_KEY, 1);
            tickets.put(R.string.BLACK_TICKET_KEY, game.getPlayers().size() - 1);

        } else {
            //set Tickets for detectives
            tickets.put(R.string.PEDESTRIAN_TICKET_KEY, 5);
            tickets.put(R.string.BICYCLE_TICKET_KEY, 4);
            tickets.put(R.string.BUS_TICKET_KEY, 2);
            tickets.put(R.string.TAXI_TICKET_KEY, 0);
            tickets.put(R.string.DOUBLE_TICKET_KEY, 0);
            tickets.put(R.string.BLACK_TICKET_KEY, 0);
        }

    }

    public void decreaseNumberOfTickets(Integer key) {
        if (!this.hasCheatedThisRound && tickets.get(key) > 0)
            tickets.put(key, tickets.get(key) - 1);
    }

    public boolean checkForValidTicket(int vehicle) {
        boolean validTicket;
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

    public boolean checkAmountOfTickets() {
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
            return false;
        }
        return true;
    }

    public int[] getRemainingTickets() {

        return new int[]{tickets.get(R.string.PEDESTRIAN_TICKET_KEY).intValue(),
                tickets.get(R.string.BICYCLE_TICKET_KEY).intValue(),
                tickets.get(R.string.BUS_TICKET_KEY).intValue(),
                tickets.get(R.string.TAXI_TICKET_KEY).intValue(),
                tickets.get(R.string.DOUBLE_TICKET_KEY).intValue(),
                tickets.get(R.string.BLACK_TICKET_KEY).intValue()};
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

    public boolean getHascheated() {
        return hascheated;
    }

    public void setHascheated(boolean hascheated) {
        this.hascheated = hascheated;
    }

    public void incCountCheatingmoves() {
        this.countCheatingmoves++;
    }

    public void decCountCheatingmoves() {
        this.countCheatingmoves--;
    }

    public int getCountCheatingmoves(){
        return this.countCheatingmoves;
    /**
     * checks if a chosen route is valid for the player
     *
     * @param newLocation position, where player wants to go
     * @return 0 if valid
     * 1 if game has end (invalid)
     * 2 if player is not active (invalid)
     * 3 if not players turn (invalid)
     * 4 if newLocation not reachable from current position (invalid)
     * 5 if bicycle not available (invalid)
     * 6 if not enough tickets (invalid)
     */
    public int isValidMove(Game game, Point newLocation) {
        if (game.getRound() > Game.getNumRounds()) {
            return 1;
        }
        if (!isActive) {
            return 2;
        }
        //if it is not players turn -> ignore move
        if (moved || (isMrX && !game.isRoundMrX()) || (!isMrX && game.isRoundMrX())) {
            return 3;
        }
        LatLng current = position.getLatLng();
        Point currentPoint = new Point(current.latitude, current.longitude);
        ValidatedRoute routeToTake = Routes.getRoute(Points.getIndex(currentPoint), Points.getIndex(newLocation));

        //if no valid route from current position to newLocation
        if (!routeToTake.isValid()) {
            return 4;
        }
        // if player has penalty and wants to take the bicycle
        if (penalty > 0 && routeToTake.getRouteType() == 1) {
            return 5;
        }
        if (!checkForValidTicket(routeToTake.getRouteType())) {
            checkAmountOfTickets();
            if (!isActive) {
                return 7;
            }
            if (!Routes.routesPossibleWithTickets(Points.getIndex(position) + 1, this)) {
                return 8;
            }
            return 6;
        }

        return 0;
    }

    public void setTickets(HashMap<Integer, Integer> tickets) {
        this.tickets = tickets;
    }

    public void resetPlayer() {
        isActive = true;
        moved = false;
        icon = 0;
        position = null;
        marker = null;
        isMrX = false;
        wantsToBeMrX = false;
        tickets = new HashMap<>();
        hasCheated = false;
        hasCheatedThisRound = false;
        penalty = 0;
    }
}
