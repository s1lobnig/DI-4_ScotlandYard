package com.example.scotlandyard.ticketTests;

import com.example.scotlandyard.Player;
import com.example.scotlandyard.R;
import com.example.scotlandyard.lobby.Game;
import com.example.scotlandyard.map.ManageGameData;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UnitTestsTickets {

    private Player testPlayer;
    private int vehicle;
    private Game game;
    private ManageGameData manageGameData;
    private HashMap<Integer, Integer> tickets;
    private ArrayList<Player> playerList;

    @Before
    public void setup() {
        testPlayer = new Player("testPlayer");
        tickets = testPlayer.getTickets();
        manageGameData = new ManageGameData();
        playerList = new ArrayList<>();
        playerList.add(testPlayer);
        playerList.add(new Player("player2"));
        playerList.add(new Player("player3"));
        playerList.add(new Player("player4"));
        game = new Game("Test", 4, 4, 5, false, playerList);
    }

    //check if hashmap has the correct size
    @Test
    public void testSizeOfHashmap() {
        Assert.assertEquals(6, tickets.size());
    }

    //check if all keys are present in the hashmap
    @Test
    public void testKeysOfHashmap() {
        Assert.assertEquals(true, tickets.containsKey(R.string.PEDESTRIAN_TICKET_KEY));
        Assert.assertEquals(true, tickets.containsKey(R.string.BICYCLE_TICKET_KEY));
        Assert.assertEquals(true, tickets.containsKey(R.string.BUS_TICKET_KEY));
        Assert.assertEquals(true, tickets.containsKey(R.string.TAXI_TICKET_KEY));
        Assert.assertEquals(true, tickets.containsKey(R.string.DOUBLE_TICKET_KEY));
        Assert.assertEquals(true, tickets.containsKey(R.string.BLACK_TICKET_KEY));
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
    public void testDetectiveTickets() {
        testPlayer.setMrX(false);
        manageGameData.setTickets(game, testPlayer);
        Assert.assertEquals(5, tickets.get(R.string.PEDESTRIAN_TICKET_KEY).intValue());
        Assert.assertEquals(4, tickets.get(R.string.BICYCLE_TICKET_KEY).intValue());
        Assert.assertEquals(2, tickets.get(R.string.BUS_TICKET_KEY).intValue());
        Assert.assertEquals(0, tickets.get(R.string.TAXI_TICKET_KEY).intValue());
        Assert.assertEquals(0, tickets.get(R.string.DOUBLE_TICKET_KEY).intValue());
        Assert.assertEquals(0, tickets.get(R.string.BLACK_TICKET_KEY).intValue());
    }

    //check mrX tickets
    @Test
    public void testMrXTickets() {
        testPlayer.setMrX(true);
        manageGameData.setTickets(game, testPlayer);
        Assert.assertEquals(Integer.MAX_VALUE, tickets.get(R.string.PEDESTRIAN_TICKET_KEY).intValue());
        Assert.assertEquals(Integer.MAX_VALUE, tickets.get(R.string.BICYCLE_TICKET_KEY).intValue());
        Assert.assertEquals(Integer.MAX_VALUE, tickets.get(R.string.BUS_TICKET_KEY).intValue());
        Assert.assertEquals(2, tickets.get(R.string.TAXI_TICKET_KEY).intValue());
        Assert.assertEquals(1, tickets.get(R.string.DOUBLE_TICKET_KEY).intValue());
        Assert.assertEquals(3, tickets.get(R.string.BLACK_TICKET_KEY).intValue());
    }

    //check if tickets get reduced after making a move
    @Test
    public void testReducingTickets() {
        manageGameData.setTickets(game, testPlayer);
        testPlayer.setMrX(false);
        testPlayer.decreaseNumberOfTickets(R.string.PEDESTRIAN_TICKET_KEY);
        testPlayer.decreaseNumberOfTickets(R.string.PEDESTRIAN_TICKET_KEY);
        Assert.assertEquals(3, testPlayer.getTickets().get(R.string.PEDESTRIAN_TICKET_KEY).intValue());
    }

    //check validity of tickets
    @Test
    public void testCheckForValidTicketDetectives() {
        manageGameData.setTickets(game, testPlayer);
        Assert.assertEquals(true, manageGameData.checkForValidTicket(testPlayer, 0));
        Assert.assertEquals(true, manageGameData.checkForValidTicket(testPlayer, 1));
        Assert.assertEquals(true, manageGameData.checkForValidTicket(testPlayer, 2));
        Assert.assertEquals(false, manageGameData.checkForValidTicket(testPlayer, 3));
        Assert.assertEquals(false, manageGameData.checkForValidTicket(testPlayer, 4));
        Assert.assertEquals(false, manageGameData.checkForValidTicket(testPlayer, 5));

        for (int i = 5; i > 0; i--) {
            testPlayer.decreaseNumberOfTickets(R.string.PEDESTRIAN_TICKET_KEY);
        }
        Assert.assertEquals(false, manageGameData.checkForValidTicket(testPlayer, 0));


        for (int i = 4; i > 0; i--) {
            testPlayer.decreaseNumberOfTickets(R.string.BICYCLE_TICKET_KEY);
        }
        Assert.assertEquals(false, manageGameData.checkForValidTicket(testPlayer, 1));

        for (int i = 2; i > 0; i--) {
            testPlayer.decreaseNumberOfTickets(R.string.BUS_TICKET_KEY);
        }
        Assert.assertEquals(false, manageGameData.checkForValidTicket(testPlayer, 2));
    }

    @Test
    public void checkForValidTicketMrX() {
        testPlayer.setMrX(true);
        manageGameData.setTickets(game, testPlayer);
        Assert.assertEquals(true, manageGameData.checkForValidTicket(testPlayer, 0));
        Assert.assertEquals(true, manageGameData.checkForValidTicket(testPlayer, 1));
        Assert.assertEquals(true, manageGameData.checkForValidTicket(testPlayer, 2));
        Assert.assertEquals(true, manageGameData.checkForValidTicket(testPlayer, 3));
        Assert.assertEquals(true, manageGameData.checkForValidTicket(testPlayer, 4));
        Assert.assertEquals(true, manageGameData.checkForValidTicket(testPlayer, 5));

        for (int i = 2; i > 0; i--) {
            testPlayer.decreaseNumberOfTickets(R.string.TAXI_TICKET_KEY);
        }
        Assert.assertEquals(false, manageGameData.checkForValidTicket(testPlayer, 3));


        testPlayer.decreaseNumberOfTickets(R.string.DOUBLE_TICKET_KEY);
        Assert.assertEquals(false, manageGameData.checkForValidTicket(testPlayer, 4));

        for (int i = 3; i > 0; i--) {
            testPlayer.decreaseNumberOfTickets(R.string.BLACK_TICKET_KEY);
        }
        Assert.assertEquals(false, manageGameData.checkForValidTicket(testPlayer, 5));

    }

    @Test
    public void testCheckAmountOfTickets() {
        testPlayer.setMrX(false);
        manageGameData.setTickets(game, testPlayer);
        for (Map.Entry<Integer, Integer> ticket : testPlayer.getTickets().entrySet()) {
            while (ticket.getValue() != 0) {
                testPlayer.decreaseNumberOfTickets(ticket.getKey());
            }
        }
        testPlayer.checkAmountOfTickets();
        Assert.assertEquals(false, testPlayer.isActive());
    }

    @Test
    public void testGetRemainingTickets() {
        testPlayer.setMrX(false);
        manageGameData.setTickets(game, testPlayer);
        testPlayer.decreaseNumberOfTickets(R.string.PEDESTRIAN_TICKET_KEY);
        testPlayer.decreaseNumberOfTickets(R.string.BUS_TICKET_KEY);

        int[] expectedTickets = {4, 4, 1, 0, 0, 0};

        Assert.assertArrayEquals(expectedTickets, testPlayer.getRemainingTickets());

    }
}

