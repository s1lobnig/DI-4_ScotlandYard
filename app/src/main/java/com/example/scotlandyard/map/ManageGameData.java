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

    public static Player findPlayer(String nickname) {
        for (Player p : Device.getGame().getPlayers()) {
            if (p.getNickname().equals(nickname)) {
                return p;
            }
        }
        return null;
    }

    public static void deactivatePlayer(Player player) {
        player.setMoved(true); //so he does not have to move in this round
        player.setActive(false);
    }

    private static boolean isRoundFinished() {
        for (Player p : Device.getGame().getPlayers()) {
            if (p.isActive() && !p.isMoved()) {
                return false;
            }
        }
        return true;
    }

    public static int tryNextRound() {
        if (isRoundFinished()) {
            if (Device.getGame().getRound() < 12) {
                //Round finished
                Device.getGame().nextRound();
                for (Player p : Device.getGame().getPlayers()) {
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
        for (Player player : Device.getGame().getPlayers()) {
            if (player.getMarker().equals(field)) {
                return true;
            }
        }
        return false;
    }

    void givePlayerPositionAndIcon() {
        Player player;
        for (int i = 0; i < Device.getGame().getPlayers().size(); i++) {
            player = Device.getGame().getPlayers().get(i);
            player.setIcon(PLAYER_ICONS[i]);
            LatLng position = getNewPlayerPosition();
            player.setPosition(new Point(position.latitude, position.longitude));
            player.setMoved(false);
            //setTickets for every player
            setTickets(Device.getGame().getPlayers().get(i));
        }
    }

    //returns a free position
    private LatLng getNewPlayerPosition() {
        int position = (new Random()).nextInt(Points.getPoints().length);
        Point point = Points.POINTS[position];
        for (Player p : Device.getGame().getPlayers()) {
            if (p.getPosition() == point) {
                return getNewPlayerPosition();
            }
        }
        return point.getLatLng();
    }

    //set and check tickets
    private void setTickets(Player player) {
        //set Ticket for Mr. X
        if (player.isMrX()) {
            player.initializeNumberOfTickets(new Object[][]{
                    {R.string.PEDESTRIAN_TICKET_KEY, 5},
                    {R.string.BICYCLE_TICKET_KEY, 4},
                    {R.string.BUS_TICKET_KEY, 2},
                    {R.string.BLACK_TICKET_KEY, Device.getGame().getPlayers().size() - 1},
            });
        } else {
            //set Tickets for all other players
            player.initializeNumberOfTickets(new Object[][]{
                    {R.string.PEDESTRIAN_TICKET_KEY, 5},
                    {R.string.BICYCLE_TICKET_KEY, 4},
                    {R.string.BUS_TICKET_KEY, 2},
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

    public void makeGame() {
        Lobby lobby = Device.getLobby();
        Game game = new Game(lobby.getLobbyName(), lobby.getMaxPlayers(), lobby.getPlayerCount(), 1, lobby.isRandomEvents(), lobby.getPlayerList());
        game.chooseMrX(lobby.isRandomMrX());
        Device.setGame(game);
        givePlayerPositionAndIcon();
    }
}
