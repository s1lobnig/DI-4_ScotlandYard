package com.example.scotlandyard;

import com.example.scotlandyard.map.Point;
import com.example.scotlandyard.map.Points;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game implements Serializable {

    private static final Random RANDOM = new SecureRandom();
    private static final int NUM_ROUNDS = 12;
    private static final int[] PLAYER_ICONS_MINION = {
            R.drawable.minion1,
            R.drawable.minion2,
            R.drawable.minion3,
            R.drawable.minion4,
            R.drawable.minion5,
            R.drawable.minion6,
            R.drawable.minion7,
            R.drawable.minion8,
            R.drawable.minion9,
            R.drawable.minion10,
            R.drawable.minion11,
            R.drawable.minion12,
            R.drawable.minion13,
            R.drawable.minion14,
    };
    private static final int[] MRX_ICONS_MINION = {
            R.drawable.minion_mr_x1
    };

    private String gameName;
    private int maxMembers;
    private int round;
    private boolean roundMrX;
    private boolean randomEventsEnabled;
    private boolean botMrX;
    private List<Player> players = new ArrayList<>(); // Changed from List to ArrayList because of serialization.


    public Game(String gameName, int maxMembers) {
        this.gameName = gameName;
        this.maxMembers = maxMembers;
    }

    public Game(String gameName, int maxMembers, int round, boolean randomEventsEnabled, boolean botMrX, List<Player> players) {
        this(gameName, maxMembers);
        this.round = round;
        this.roundMrX = true;
        this.randomEventsEnabled = randomEventsEnabled;
        this.botMrX = botMrX;
        this.players = players;
    }

    public static int getNumRounds() {
        return NUM_ROUNDS;
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

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    /**
     * @return Player who is MrX
     * returns null, if no MrX in the Game
     */
    public Player getMrX() {
        for (Player p : players) {
            if (p.isMrX()) {
                return p;
            }
        }
        return null;
    }

    /**
     * @return Bot if bot is anabled an exists, if not then null will be returned
     */
    public Player getBotMrX() {
        if (botMrX) {
            return getMrX();
        }
        return null;
    }

    /**
     * Finds Player with given name in the game
     *
     * @param nickname name of player
     * @return Player if one with this name exists, otherwise returns null
     */
    public Player findPlayer(String nickname) {
        for (Player p : players) {
            if (p.getNickname().equals(nickname)) {
                return p;
            }
        }
        return null;
    }

    /**
     * checks if a round is finished
     *
     * @return true if round has end and next round can start
     */
    private boolean isRoundFinished() {
        for (Player p : players) {
            if (p.isActive() && !p.isMoved()) {
                return false;
            }
        }
        return true;
    }

    /**
     * deactivates the given player and returns a value to check if next round can be started
     *
     * @param player
     * @return -1 if round has not finished
     * 1 if round has finished
     * 0 if game ends because last round was reached
     */
    public int deactivatePlayer(Player player) {
        if (player.isMrX()) {
            setBotMrX(true);
        }
        player.setActive(false);
        return tryNextRound();
    }

    /**
     * trys if possible to start a new round
     *
     * @return -1 if round has not finished
     * 1 if round has finished
     * 0 if game ends because last round was reached
     * 2 if no detective can move anymore
     */
    public int tryNextRound() {
        if (isRoundMrX() && getMrX().isMoved()) {
            setRoundMrX(false);
        }
        if (isRoundFinished()) {
            if (round < NUM_ROUNDS) {
                /*
                If no detective can move anymore, Mr.X has won
                 */
                round++;
                if (allDetectivesStuck()) {
                    return 2;
                }
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

    public boolean allDetectivesStuck() {
        for (Player p : players) {
            if (!p.isMrX() && p.isActive()) {
                return false;
            }
        }
        return true;
    }

    /**
     * checks if a giver marker belongs to a Player of this game
     *
     * @param marker marker to check
     * @return true if this marker belongs to a Player, false if not
     */
    public boolean isPlayer(Marker marker) {
        for (Player player : players) {
            if (player.getMarker().equals(marker)) {
                return true;
            }
        }
        return false;
    }

    /**
     * gives all player of the game icon, position and tickets
     */
    public void givePlayerPositionAndIcon() {
        Player player;
        for (int i = 0; i < players.size(); i++) {
            player = players.get(i);
            if (player.isMrX()) {
                player.setIcon(MRX_ICONS_MINION[0]);
            } else {
                player.setIcon(PLAYER_ICONS_MINION[i]);
            }
            LatLng position = getNewPlayerPosition();
            player.setPosition(new Point(position.latitude, position.longitude));

            //setTickets for every player
            player.setTickets(this);
        }
    }

    //returns a free position
    private LatLng getNewPlayerPosition() {
        int position = RANDOM.nextInt(Points.getFields().length);
        Point point = Points.getFields()[position];
        for (Player p : players) {
            if (point.equals(p.getPosition())) {
                return getNewPlayerPosition();
            }
        }
        return point.getLatLng();
    }

    public boolean checkIfMrxHasLost() {
        for (Player p : players) {
            if (getMrX().getNickname() != p.getNickname() && getMrX().getPosition().equals(p.getPosition())) {
                return true;
            }
        }
        return false;
    }
}
