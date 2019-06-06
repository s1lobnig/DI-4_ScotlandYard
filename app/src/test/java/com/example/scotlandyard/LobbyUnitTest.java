package com.example.scotlandyard;

import com.example.scotlandyard.lobby.Lobby;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
/**
 * Unit tests for the lobby class
 */
public class LobbyUnitTest {
    private Player p1;
    private Player p2;
    private Player p3;
    private Lobby lobby;

    @Before
    public void setUp() {
        p1 = new Player("herbert");
        p2 = new Player("thomas");
        p3 = new Player("sarah");
        lobby = new Lobby("testLobby", new ArrayList<Player>(), true, false, 5);
    }

    @After
    public void tearDown() {
        p1 = null;
        p2 = null;
        p3 = null;
        lobby = null;
    }

    @Test
    public void testAddPlayer() {
        lobby.addPlayer(p1);
        lobby.addPlayer(p2);
        ArrayList<Player> arrList = new ArrayList<>();
        arrList.add(p1);
        arrList.add(p2);
        Assert.assertEquals(arrList, lobby.getPlayerList());
    }

    @Test
    public void testRandomEvents() {
        Assert.assertTrue(lobby.isRandomEvents());
    }

    @Test
    public void testMrX() {
        Assert.assertFalse(lobby.isRandomMrX());
    }

    @Test
    public void testPlayerCount() {
        lobby.addPlayer(p1);
        lobby.addPlayer(p2);
        Assert.assertEquals(2, lobby.getPlayerCount());
    }

    @Test
    public void testMaxPlayers() {
        Assert.assertEquals(5, lobby.getMaxPlayers());
    }

    @Test
    public void testLobbyName() {
        Assert.assertEquals("testLobby", lobby.getLobbyName());
    }

    @Test
    public void testRemovePlayer() {
        lobby.addPlayer(p1);
        lobby.addPlayer(p2);
        lobby.addPlayer(p3);
        lobby.removePlayer("thomas");
        lobby.removePlayer("herbert");
        ArrayList<Player> arrList = new ArrayList<>();
        arrList.add(p3);
        Assert.assertEquals(arrList, lobby.getPlayerList());
    }

    @Test
    public void testNickAlreadyUsed() {
        lobby.addPlayer(p1);
        lobby.addPlayer(p2);
        lobby.addPlayer(p3);
        Assert.assertTrue(lobby.nickAlreadyUsed("thomas"));
    }

    @Test
    public void testNickAlreadyUsedFalse() {
        lobby.addPlayer(p1);
        lobby.addPlayer(p2);
        lobby.addPlayer(p3);
        Assert.assertFalse(lobby.nickAlreadyUsed("stefan"));
    }

    @Test
    public void testChooseMrXRandom() {
        lobby.addPlayer(p1);
        lobby.addPlayer(p2);
        lobby.addPlayer(p3);
        lobby.chooseMrX(true);
        int countMrX = 0;
        for (Player p : lobby.getPlayerList()) {
            if (p.isMrX()) {
                countMrX++;
            }
        }
        Assert.assertEquals(1, countMrX);
    }

    @Test
    public void testChooseMrXNormal() {
        p2.setWantsToBeMrX(true);
        lobby.addPlayer(p1);
        lobby.addPlayer(p2);
        lobby.addPlayer(p3);
        lobby.chooseMrX(false);
        Assert.assertTrue(lobby.getPlayerList().get(1).isMrX());
    }
}