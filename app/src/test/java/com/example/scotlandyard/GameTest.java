package com.example.scotlandyard;

import com.example.scotlandyard.lobby.Lobby;
import com.google.android.gms.maps.model.Marker;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GameTest {
    Game game;
    Player p1;
    Player p2;
    Player p3;
    ArrayList<Player> playerlist;

    @Before
    public void setUp() {
        p1 = new Player("player1");
        p2 = new Player("player2");
        p3 = new Player("player3");
        p1.setMrX(true);
        playerlist = new ArrayList<>();
        playerlist.add(p1);
        playerlist.add(p2);
        playerlist.add(p3);

        game = new Game("Test", 4, 5, false, false, playerlist);
    }

    @Test
    public void testConstructor() {
        assertEquals("Test", game.getGameName());
        assertEquals(4, game.getMaxMembers());
        assertEquals(5, game.getRound());
        assertTrue(game.isRoundMrX());
        assertFalse(game.isRandomEventsEnabled());
        assertFalse(game.isBotMrX());
        assertEquals(playerlist, game.getPlayers());
    }

    @Test
    public void testGetMrX() {
        Player mrX = game.getMrX();
        assertEquals(p1, mrX);
    }

    @Test
    public void testGetBotMrXtrue() {
        game.setBotMrX(true);
        Player bot = game.getBotMrX();
        assertEquals(p1, bot);
    }

    @Test
    public void testGetBotMrXfalse() {
        Player bot = game.getBotMrX();
        assertNull(bot);
    }

    @Test
    public void testFindPlayerTrue() {
        Player p = game.findPlayer("player1");
        assertEquals("player1", p.getNickname());
    }

    @Test
    public void testFindPlayerFalse() {
        Player p = game.findPlayer("someone");
        assertNull(p);
    }

    @Test
    public void testDeactivatePlayer() {
        int result = game.deactivatePlayer(p2);
        assertFalse(p2.isActive());
        assertEquals(-1, result);
    }

    @Test
    public void testDeactivatePlayerMrX() {
        int result = game.deactivatePlayer(p1);
        assertFalse(p1.isActive());
        assertTrue(game.isBotMrX());
        assertEquals(-1, result);
    }

    @Test
    public void testDeactivatePlayerNewRound() {
        p1.setMoved(true);
        p3.setMoved(true);
        int result = game.deactivatePlayer(p2);

        assertFalse(p2.isActive());
        assertEquals(6, game.getRound());
        assertEquals(1, result);
    }

    @Test
    public void testDeactivatePlayerGameFinished() {
        game.setRound(Game.getNumRounds());
        p1.setMoved(true);
        p3.setMoved(true);
        int result = game.deactivatePlayer(p2);

        assertFalse(p2.isActive());
        assertEquals(Game.getNumRounds() + 1, game.getRound());
        assertEquals(0, result);
    }

    private void assertAllPlayerIsMovedFalse() {
        for (Player player : game.getPlayers()) {
            assertFalse(player.isMoved());
        }
    }

    @Test
    public void testTryNextRoundTrue() {
        p1.setMoved(true);
        p2.setMoved(true);
        p3.setMoved(true);

        int result = game.tryNextRound();
        assertEquals(1, result);
        assertEquals(6, game.getRound());
        assertAllPlayerIsMovedFalse();
    }

    @Test
    public void testTryNextRoundDeactivatedPlayer() {
        p1.setMoved(true);
        p2.setMoved(true);
        p3.setMoved(false);
        p3.setActive(false);

        int result = game.tryNextRound();
        assertEquals(1, result);
        assertEquals(6, game.getRound());
        assertAllPlayerIsMovedFalse();
    }

    @Test
    public void testTryNextRoundFalse() {
        p1.setMoved(true);
        p2.setMoved(false);
        p3.setMoved(true);

        int result = game.tryNextRound();
        assertEquals(-1, result);
        assertEquals(5, game.getRound());
    }

    @Test
    public void testTryNextRoundEnd() {
        game.setRound(Game.getNumRounds());
        p1.setMoved(true);
        p2.setMoved(true);
        p3.setMoved(true);

        int result = game.tryNextRound();
        assertEquals(0, result);
        assertEquals(Game.getNumRounds() + 1, game.getRound());
    }

    @Test
    public void testGivePlayerPositionAndIcon() {
        game.givePlayerPositionAndIcon();

        for (Player player : game.getPlayers()) {
            assertNotNull(player.getIcon());
            assertNotNull(player.getPosition());
            for (Player p : game.getPlayers()) {
                if (!player.equals(p)) {
                    assertNotEquals(player.getPosition(), p.getPosition());
                }
            }
            //ToDo: test if player has right amount of tickets @TheExecutor
        }
    }

    public void testTryNextRoundAllStuck() {
        p2.setActive(false);
        p3.setActive(false);
        Assert.assertEquals(2, game.tryNextRound());
    }

    public void testAllDetectivesStuckTrue() {
        p2.setActive(false);
        p3.setActive(false);
        Assert.assertTrue(game.allDetectivesStuck());
    }

    public void testAllDetectivesStuckFalse() {
        p2.setActive(false);
        p3.setActive(true);
        Assert.assertFalse(game.allDetectivesStuck());
    }

    @After
    public void tearDown() {
        game = null;
    }
}
