package com.example.scotlandyard;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GameTest {
    Game game;
    Player mrX;
    Player detective1;
    Player detective2;
    ArrayList<Player> playerlist;

    @Before
    public void setUp() {
        mrX = new Player("player1");
        detective1 = new Player("player2");
        detective2 = new Player("player3");
        mrX.setMrX(true);
        playerlist = new ArrayList<>();
        playerlist.add(mrX);
        playerlist.add(detective1);
        playerlist.add(detective2);

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
        assertEquals(this.mrX, mrX);
    }

    @Test
    public void testGetBotMrXtrue() {
        game.setBotMrX(true);
        Player bot = game.getBotMrX();
        assertEquals(mrX, bot);
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
        int result = game.deactivatePlayer(detective1);
        assertFalse(detective1.isActive());
        assertEquals(-1, result);
    }

    @Test
    public void testDeactivatePlayerMrX() {
        int result = game.deactivatePlayer(mrX);
        assertFalse(mrX.isActive());
        assertTrue(game.isBotMrX());
        assertEquals(-1, result);
    }

    @Test
    public void testDeactivatePlayerNewRound() {
        mrX.setMoved(true);
        detective2.setMoved(true);
        int result = game.deactivatePlayer(detective1);

        assertFalse(detective1.isActive());
        assertEquals(6, game.getRound());
        assertEquals(1, result);
    }

    @Test
    public void testDeactivatePlayerGameFinished() {
        game.setRound(Game.getNumRounds());
        mrX.setMoved(true);
        detective2.setMoved(true);
        int result = game.deactivatePlayer(detective1);

        assertFalse(detective1.isActive());
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
        mrX.setMoved(true);
        detective1.setMoved(true);
        detective2.setMoved(true);

        int result = game.tryNextRound();
        assertEquals(1, result);
        assertEquals(6, game.getRound());
        assertAllPlayerIsMovedFalse();
    }

    @Test
    public void testTryNextRoundDeactivatedPlayer() {
        mrX.setMoved(true);
        detective1.setMoved(true);
        detective2.setMoved(false);
        detective2.setActive(false);

        int result = game.tryNextRound();
        assertEquals(1, result);
        assertEquals(6, game.getRound());
        assertAllPlayerIsMovedFalse();
    }

    @Test
    public void testTryNextRoundAllDetectivesDeactivated() {
        mrX.setMoved(true);
        detective1.setActive(false);
        detective2.setActive(false);

        int result = game.tryNextRound();
        assertEquals(2, result);
    }

    @Test
    public void testTryNextRoundFalse() {
        mrX.setMoved(true);
        detective1.setMoved(false);
        detective2.setMoved(true);

        int result = game.tryNextRound();
        assertEquals(-1, result);
        assertEquals(5, game.getRound());
    }

    @Test
    public void testTryNextRoundEnd() {
        game.setRound(Game.getNumRounds());
        mrX.setMoved(true);
        detective1.setMoved(true);
        detective2.setMoved(true);

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
        }
    }

    public void testTryNextRoundAllStuck() {
        detective1.setActive(false);
        detective2.setActive(false);
        Assert.assertEquals(2, game.tryNextRound());
    }

    public void testAllDetectivesStuckTrue() {
        detective1.setActive(false);
        detective2.setActive(false);
        Assert.assertTrue(game.allDetectivesStuck());
    }

    public void testAllDetectivesStuckFalse() {
        detective1.setActive(false);
        detective2.setActive(true);
        Assert.assertFalse(game.allDetectivesStuck());
    }

    @After
    public void tearDown() {
        game = null;
    }
}
