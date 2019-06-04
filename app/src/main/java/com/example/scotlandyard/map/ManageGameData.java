package com.example.scotlandyard.map;

import com.example.scotlandyard.Player;
import com.example.scotlandyard.R;
import com.example.scotlandyard.control.Device;
import com.example.scotlandyard.lobby.Game;
import com.example.scotlandyard.lobby.Lobby;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.Random;

public class ManageGameData {

    public static void setTickets(Game game, Player player) {
        //set Ticket for Mr. X
        if (player.isMrX()) {
            player.initializeNumberOfTickets(new Object[][]{
                    {R.string.PEDESTRIAN_TICKET_KEY, Integer.MAX_VALUE},
                    {R.string.BICYCLE_TICKET_KEY, Integer.MAX_VALUE},
                    {R.string.BUS_TICKET_KEY, Integer.MAX_VALUE},
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

    public static boolean checkForValidTicket(Player player, int vehicle) {
        boolean validTicket = false;
        HashMap<Integer, Integer> tickets = player.getTickets();
        switch (vehicle) {
            case 0:
                if (tickets.get(R.string.PEDESTRIAN_TICKET_KEY) > 0) {
                    validTicket = true;
                }
                break;
            case 1:
                if (tickets.get(R.string.BICYCLE_TICKET_KEY) > 0) {
                    validTicket = true;
                }
                break;
            case 2:
                if (tickets.get(R.string.BUS_TICKET_KEY) > 0) {
                    validTicket = true;
                }
                break;
            case 3:
                if (tickets.get(R.string.TAXI_TICKET_KEY) > 0) {
                    validTicket = true;
                }
                break;
            case 4:
                if (tickets.get(R.string.DOUBLE_TICKET_KEY) > 0) {
                    validTicket = true;
                }
                break;

            case 5:
                if (tickets.get(R.string.BLACK_TICKET_KEY) > 0) {
                    validTicket = true;

                }
                break;
            default:

        }
        return validTicket;
    }
}
