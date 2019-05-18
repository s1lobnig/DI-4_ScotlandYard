package com.example.scotlandyard.map;

import com.example.scotlandyard.Player;
import com.example.scotlandyard.R;
import com.example.scotlandyard.lobby.Game;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.Random;

public class ManageGameData {
    static Game game;
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

    Player findPlayer(String nickname) {
        for (Player p : game.getPlayers()) {
            if (p.getNickname().equals(nickname)) {
                return p;
            }
        }
        return null;
    }

    void deactivatePlayer(Player player) {
        player.setMoved(true); //so he does not have to move in this round
        player.setActive(false);
    }

    private boolean isRoundFinished() {
        for (Player p : game.getPlayers()) {
            if (p.isActive() && !p.isMoved()) {
                return false;
            }
        }
        return true;
    }

    int tryNextRound() {
        if (isRoundFinished()) {
            if (game.getRound() < 12) {
                //Round finished
                game.nextRound();
                for (Player p : game.getPlayers()) {
                    p.setMoved(false);
                }
                return 1;
            }
            //Game finished
            return 0;
        }
        //Round not finished yet
        return -1;
    }

    boolean isPlayer(Marker field) {
        for (Player player : game.getPlayers()) {
            if (player.getMarker().equals(field)) {
                return true;
            }
        }
        return false;
    }

    void givePlayerPositionAndIcon() {
        Player player;
        for (int i = 0; i < game.getPlayers().size(); i++) {
            player = game.getPlayers().get(i);
            player.setIcon(PLAYER_ICONS[i]);
            LatLng position = getNewPlayerPosition();
            player.setPosition(new Point(position.latitude, position.longitude));
            player.setMoved(false);
            //setTickets for every player
            setTickets(game.getPlayers().get(i));
        }
    }

    //returns a free position
    private LatLng getNewPlayerPosition() {
        int position = (new Random()).nextInt(Points.getPoints().length);
        Point point = Points.POINTS[position];
        for (Player p : game.getPlayers()) {
            if (p.getPosition() == point) {
                return getNewPlayerPosition();
            }
        }
        return point.getLatLng();
    }

    private void setTickets(Player player) {
        //set Ticket for Mr. X
        if (player.isMrX()) {
            player.initializeNumberOfTickets(new Object[][]{
                    {R.string.PEDESTRIAN_TICKET_KEY, 5},
                    {R.string.BICYCLE_TICKET_KEY, 4},
                    {R.string.BUS_TICKET_KEY, 2},
                    {R.string.TAXI_TICKET_KEY, 2},
                    {R.string.DOUBLE_TICKET_KEY, 1},
                    {R.string.BLACK_TICKET_KEY, game.getPlayers().size() - 1},
            });
        } else {
            //set Tickets for all other players
            player.initializeNumberOfTickets(new Object[][]{
                    {R.string.PEDESTRIAN_TICKET_KEY, 5},
                    {R.string.BICYCLE_TICKET_KEY, 4},
                    {R.string.BUS_TICKET_KEY, 2},
                    {R.string.TAXI_TICKET_KEY, 0},
                    {R.string.DOUBLE_TICKET_KEY, 0},
                    {R.string.BLACK_TICKET_KEY, 0},
            });
        }

    }

    boolean checkForValidTicket(Player player, int vehicle) {
        boolean validTicket = false;
        HashMap<Integer, Integer> tickets = player.getTickets();
        switch (vehicle) {
            case 0:
                if (tickets.get(R.string.PEDESTRIAN_TICKET_KEY) > 0) {
                    validTicket = true;
                    player.decreaseNumberOfTickets(R.string.PEDESTRIAN_TICKET_KEY);
                }
                break;
            case 1:
                if (tickets.get(R.string.BICYCLE_TICKET_KEY) > 0) {
                    validTicket = true;
                    player.decreaseNumberOfTickets(R.string.BICYCLE_TICKET_KEY);
                }
                break;
            case 2:
                if (tickets.get(R.string.BUS_TICKET_KEY) > 0) {
                    validTicket = true;
                    player.decreaseNumberOfTickets(R.string.BUS_TICKET_KEY);
                }
                break;
            case 3:
                if (tickets.get(R.string.BLACK_TICKET_KEY) > 0) {
                    validTicket = true;
                    player.decreaseNumberOfTickets(R.string.BLACK_TICKET_KEY);
                }
                break;
            default:
                validTicket = false;
        }
        return validTicket;
    }
}
