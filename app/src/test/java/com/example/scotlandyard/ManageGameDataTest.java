package com.example.scotlandyard;

import com.example.scotlandyard.control.Device;
import com.example.scotlandyard.lobby.Game;
import com.example.scotlandyard.lobby.Lobby;
import com.example.scotlandyard.map.ManageGameData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ManageGameDataTest {
    Game game;
    ArrayList<Player> playerlist;
    Player p1;
    Player p2;
    Player p3;

    @Before
    public void setUp(){
        p1 = new Player("player1");
        p2 = new Player("player2");
        p3 = new Player("player3");
        playerlist = new ArrayList<>();
        playerlist.add(p1);
        playerlist.add(p2);
        playerlist.add(p3);

        game = new Game("Test", 4, 3, 5, false, playerlist);
    }

    @Test
    public void testFindPlayerTrue(){
        Player p = ManageGameData.findPlayer(game, "player1");
        assertEquals("player1", p.getNickname());
    }

    @Test
    public void testFindPlayerFalse(){
        Player p = ManageGameData.findPlayer(game, "someone");
        assertNull(p);
    }

    @Test
    public void testDeactivatePlayer(){
        p1.setMoved(false);
        ManageGameData.deactivatePlayer(game, p1);
        assertTrue(p1.isMoved());
        assertFalse(p1.isActive());
    }

    @Test
    public void testTryNextRoundTrue(){
        p1.setMoved(true);
        p2.setMoved(true);
        p3.setMoved(true);

        int result = ManageGameData.tryNextRound(game);
        assertEquals(1, result);
        assertEquals(6, game.getRound());
    }

    @Test
    public void testTryNextRoundTrueDeacivePlayer(){
        p1.setMoved(true);
        p2.setMoved(false);
        p2.setActive(false);
        p3.setMoved(true);

        int result = ManageGameData.tryNextRound(game);
        assertEquals(1, result);
        assertEquals(6, game.getRound());
    }

    @Test
    public void testTryNextRoundFalse(){
        p1.setMoved(true);
        p2.setMoved(false);
        p3.setMoved(true);

        int result = ManageGameData.tryNextRound(game);
        assertEquals(-1, result);
        assertEquals(5, game.getRound());
    }

    @Test
    public void testTryNextRoundEnd(){
        p1.setMoved(true);
        p2.setMoved(true);
        p3.setMoved(true);
        game.setRound(12);

        int result = ManageGameData.tryNextRound(game);
        assertEquals(0, result);
        assertEquals(12, game.getRound());
    }

    @Test
    public void testmakeGame(){
        Lobby lobby = new Lobby("Test", playerlist, false, false, 4);
        game = ManageGameData.makeGame(lobby);

        assertEquals(lobby.getLobbyName(), game.getGameName());
        assertEquals(lobby.getMaxPlayers(), game.getMaxMembers());
        assertEquals(lobby.getPlayerCount(), game.getCurrentMembers());
        assertEquals(1, game.getRound());
        assertEquals(lobby.isRandomEvents(), game.isRandomEventsEnabled());
        assertEquals(lobby.getPlayerList(), game.getPlayers());
        checkMisterX(game);
        checkMarkerPositionIcon();
    }

    private void checkMarkerPositionIcon() {
        for (Player p : game.getPlayers()){
            assertNotNull(p.getIcon());
            assertNotNull(p.getPosition());
            assertNotNull(p.getTickets());
        }
    }

    private void checkMisterX(Game game) {
        for (Player p : game.getPlayers()){
            if(p.isMrX()){
               return;
            }
        }
        fail();
    }

    @After
    public void tearDown(){
        game = null;
    }
}
