package com.example.scotlandyard.ticketTests;

import com.example.scotlandyard.Player;
import com.example.scotlandyard.map.ManageGameData;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class checkValidTicketParameterTest {

    private static Player testPlayer;
    private int key;
    private int value;
    private ManageGameData manageGameData;

    public checkValidTicketParameterTest(int key, int value){
        super();
        this.key = key;
        this.value = value;
    }

    @Parameters
    public static Object[][] createHashMap(){
        Object[][] tickets = new Object[][]{
                {0, 0},
                {1, 0},
                {2, 0},
                {3, 0}};
        return tickets;
    }

    @Before
    public void setup() {
        testPlayer = new Player("testPlayer");
        manageGameData = new ManageGameData();
    }
    //check not valid detective tickets
    public void testNotEnoughTicketsDetectives(){
        Assert.assertEquals(false, manageGameData.checkForValidTicket(testPlayer, key));
    }

    //TODO: check not valid MrX tickets. Need do implement taxi and double tickets in method
    //check not valid mrX tickets
    public void testNotEnoughTicketsMrX(){
        testPlayer.setMrX(true);
        Assert.assertEquals(false, manageGameData.checkForValidTicket(testPlayer, key));
    }

    //check for validTicket when there are not enough tickets
    @Test
    public void testValidTickets(){
        testNotEnoughTicketsDetectives();
        testNotEnoughTicketsMrX();
    }
}
