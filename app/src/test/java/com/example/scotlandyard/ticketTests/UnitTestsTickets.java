package com.example.scotlandyard.ticketTests;

import com.example.scotlandyard.Player;
import com.example.scotlandyard.R;
import com.example.scotlandyard.lobby.Game;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class UnitTestsTickets {

    private Player testPlayer;
    private Game game;
    private HashMap<Integer, Integer> tickets;
    private ArrayList<Player> playerList;

    @Before
    public void setup() {
        testPlayer = new Player("testPlayer");
        tickets = testPlayer.getTickets();
        playerList.add(testPlayer);
        playerList.add(new Player("player2"));
        playerList.add(new Player("player3"));
        playerList.add(new Player("player4"));
    }

    //check if hashmap has the correct size
    @Test
    public void testSizeOfHashmap() {
        Assert.assertEquals(6, tickets.size());
    }

    //check if all keys are present in the hashmap
    @Test
    public void testKeysOfHashmap() {
        Assert.assertEquals(true, tickets.containsKey("pedestrian_tickets"));
        Assert.assertEquals(true, tickets.containsKey("bicycle_tickets"));
        Assert.assertEquals(true, tickets.containsKey("bus_tickets"));
        Assert.assertEquals(true, tickets.containsKey("taxi_tickets"));
        Assert.assertEquals(true, tickets.containsKey("black_tickets"));
        Assert.assertEquals(true, tickets.containsKey("double_tickets"));
    }

    //check if after initializing a player, all tickets are null
    @Test
    public void testInitializePlayer() {
        for (int key : tickets.keySet()) {
            Assert.assertEquals(0, tickets.get(key).intValue());
        }
    }

    //check detective tickets
    @Test
    public void testDetectiveTickets(){
        testPlayer.setMrX(false);
        Assert.assertEquals(5, tickets.get(R.string.PEDESTRIAN_TICKET_KEY).intValue());
        Assert.assertEquals(4, tickets.get(R.string.BICYCLE_TICKET_KEY).intValue());
        Assert.assertEquals(2, tickets.get(R.string.BUS_TICKET_KEY).intValue());
        Assert.assertEquals(0, tickets.get(R.string.TAXI_TICKET_KEY).intValue());
        Assert.assertEquals(0, tickets.get(R.string.DOUBLE_TICKET_KEY).intValue());
        Assert.assertEquals(0, tickets.get(R.string.BLACK_TICKET_KEY).intValue());
    }

    //check mrX tickets
    @Test
    public void testMrXTickets(){
        testPlayer.setMrX(true);
        Assert.assertEquals(5, tickets.get(R.string.PEDESTRIAN_TICKET_KEY).intValue());
        Assert.assertEquals(4, tickets.get(R.string.BICYCLE_TICKET_KEY).intValue());
        Assert.assertEquals(2, tickets.get(R.string.BUS_TICKET_KEY).intValue());
        Assert.assertEquals(2, tickets.get(R.string.TAXI_TICKET_KEY).intValue());
        Assert.assertEquals(1, tickets.get(R.string.DOUBLE_TICKET_KEY).intValue());
        Assert.assertEquals(5, tickets.get(R.string.BLACK_TICKET_KEY).intValue());
    }
}
