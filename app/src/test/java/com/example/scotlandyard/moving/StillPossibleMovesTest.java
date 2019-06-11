package com.example.scotlandyard.moving;

import com.example.scotlandyard.Player;
import com.example.scotlandyard.R;
import com.example.scotlandyard.Game;
import com.example.scotlandyard.map.Points;
import com.example.scotlandyard.map.Route;
import com.example.scotlandyard.map.Routes;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.example.scotlandyard.map.routetypes.BicycleRoutes.BICYCLE;
import static com.example.scotlandyard.map.routetypes.FootRoutes.BY_FOOT;

@RunWith(Parameterized.class)
public class StillPossibleMovesTest {
    @Parameterized.Parameters(name = "{index}: Player at {1}, isMrX = {0}, should be able to move: {3}, player active: {4}")
    public static Iterable data() {
        return Arrays.asList(new Object[][]{
                /*
                isMrX
                player position
                tickets
                should be able to move
                player active
                */
                /* Player is not MrX */
                {false, 31, new int[]{0, 4, 2, 0, 0, 0}, true, true},
                {false, 56, new int[]{0, 4, 2, 0, 0, 0}, false, false},
                {false, 31, new int[]{5, 0, 2, 0, 0, 0}, true, true},
                {false, 65, new int[]{0, 0, 2, 0, 0, 0}, false, false},
                {false, 31, new int[]{0, 4, 0, 0, 0, 0}, true, true},
                {false, 31, new int[]{0, 0, 0, 0, 0, 0}, false, false},
                /* Player is MrX */
                {true, 31, new int[]{0, 4, 2, 0, 0, 0}, true, true},
                {true, 56, new int[]{0, 4, 2, 0, 0, 0}, false, false},
                {true, 31, new int[]{5, 0, 2, 0, 0, 0}, true, true},
                {true, 65, new int[]{0, 0, 2, 0, 0, 0}, false, false},
                {true, 31, new int[]{0, 4, 0, 0, 0, 0}, true, true},
                {true, 31, new int[]{0, 0, 0, 0, 0, 0}, false, false},
                {true, 12, new int[]{5, 4, 0, 0, 0, 0}, true, true},
                {true, 12, new int[]{0, 4, 2, 0, 0, 0}, true, true},
                {true, 12, new int[]{0, 4, 0, 2, 0, 0}, true, true},
                {true, 12, new int[]{0, 4, 0, 0, 0, 0}, false, false},
        });
    }

    private static final String playerNick = "test";
    private Player player;
    private int currentPosition;
    private HashMap<Integer, Integer> tickets;
    private boolean expectedResut;
    private boolean playerDeactivated;
    private boolean result;
    private boolean isActive;

    public StillPossibleMovesTest(boolean isMrX, int position, int[] tickets, boolean expectedResut, boolean playerDeactivated) {
        this.tickets = new HashMap<>();
        setTickets(tickets);
        this.player = new Player(playerNick);
        player.setMrX(isMrX);
        player.setPosition(Points.POINTS[position - 1]);
        player.setTickets(this.tickets);
        this.currentPosition = position;
        this.expectedResut = expectedResut;
        this.playerDeactivated = playerDeactivated;
    }

    private void setTickets(int[] tickets) {
        this.tickets.put(R.string.PEDESTRIAN_TICKET_KEY, tickets[0]);
        this.tickets.put(R.string.BICYCLE_TICKET_KEY, tickets[1]);
        this.tickets.put(R.string.BUS_TICKET_KEY, tickets[2]);
        this.tickets.put(R.string.TAXI_TICKET_KEY, tickets[3]);
        this.tickets.put(R.string.DOUBLE_TICKET_KEY, tickets[4]);
        this.tickets.put(R.string.BLACK_TICKET_KEY, tickets[5]);
    }

    @Before
    public void setUp() {
        this.result = Routes.routesPossibleWithTickets(this.currentPosition, this.player);
        this.isActive = this.player.isActive();
    }

    @Test
    public void checkRoutesPossibleWithTickets() {
        Assert.assertEquals(expectedResut, result);
    }

    @Test
    public void testIfActive() {
        Assert.assertEquals(playerDeactivated, isActive);
    }
}

