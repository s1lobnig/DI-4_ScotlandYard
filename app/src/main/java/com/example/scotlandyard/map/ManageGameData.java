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

    public static Player findPlayer(Game game, String nickname) {
        for (Player p : game.getPlayers()) {
            if (p.getNickname().equals(nickname)) {
                return p;
            }
        }
        return null;
    }

    private static boolean isRoundFinished(Game game) {
        for (Player p : game.getPlayers()) {
            if (p.isActive() && !p.isMoved()) {
                return false;
            }
        }
        return true;
    }

    public static void deactivatePlayer(Game game, Player player){
        player.setActive(false);
        tryNextRound(game);
    }

    public static int tryNextRound(Game game) {
        if (game.isRoundMrX()) {
            if (game.getMrX().isMoved()) {
                game.setRoundMrX(false);
                //return -1;
            }
        }
        if (isRoundFinished(game)) {
            if (game.getRound() < Game.getNumRounds()) {
                /*if(this.isMrXFound()){
                    return 2;
                }*/
                //Round finished
                //check if all players have enough tickets
                for(Player p : game.getPlayers()){
                    p.checkAmountOfTickets();
                }
                game.nextRound();
                game.setRoundMrX(true);
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
    /*private boolean isMrXFound(){
        Player x = this.getMrX();
        for(Player p : game.getPlayers()){
            if (p.getPosition() == x.getPosition() && p.getNickname() != x.getNickname())
                return true;
        }
        return false;
    }*/

    private Player getMrX() {
        for (Player p : game.getPlayers()) {
            if (p.isMrX())
                return p;
        }
        return null;
    }


    static boolean isPlayer(Game game, Marker field) {
        for (Player player : game.getPlayers()) {
            if (player.getMarker().equals(field)) {
                return true;
            }
        }
        return false;
    }

    private static void givePlayerPositionAndIcon(Game game) {
        Player player;
        for (int i = 0; i < game.getPlayers().size(); i++) {
            player = game.getPlayers().get(i);
            player.setIcon(PLAYER_ICONS[i]);
            LatLng position = getNewPlayerPosition(game);
            player.setPosition(new Point(position.latitude, position.longitude));
            player.setMoved(false);
            //setTickets for every player
            setTickets(game, game.getPlayers().get(i));
        }
    }

    //returns a free position
    private static LatLng getNewPlayerPosition(Game game) {
        int position = (new Random()).nextInt(Points.getPoints().length);
        Point point = Points.POINTS[position];
        for (Player p : game.getPlayers()) {
            if (p.getPosition() == point) {
                return getNewPlayerPosition(game);
            }
        }
        return point.getLatLng();
    }

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
                    //player.decreaseNumberOfTickets(R.string.DOUBLE_TICKET_KEY);
                }
                break;

            case 5:
                if (tickets.get(R.string.BLACK_TICKET_KEY) > 0) {
                    validTicket = true;
                    //player.decreaseNumberOfTickets(R.string.BLACK_TICKET_KEY);
                }
                break;
            default:
                validTicket = false;
        }
        return validTicket;
    }

    public static Game makeGame(Lobby lobby) {
        lobby.chooseMrX(lobby.isRandomMrX());
        Game game = new Game(lobby.getLobbyName(), lobby.getMaxPlayers(), lobby.getPlayerCount(), 1, lobby.isRandomEvents(), lobby.getPlayerList());
        givePlayerPositionAndIcon(game);
        return game;
    }
}
