package com.example.scotlandyard.playerTests;

import com.example.scotlandyard.Game;
import com.example.scotlandyard.Player;
import com.example.scotlandyard.map.Point;
import com.example.scotlandyard.map.Points;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PlayerIsValidMoveTest {
    Point reachable;
    Player player;
    Player player2;
    Game game;

    @Before
    public void setup(){
        player = new Player("Sophie");
        player.setPosition(Points.getPoints()[30]);
        player2 = new Player("Tim");
        player2.setPosition(Points.getPoints()[30]);
        Player.setTickets(game, player);

        ArrayList<Player> players = new ArrayList<>();
        players.add(player);
        players.add(player2);

        game = new Game("Game", 3, 3, false, false, players);
        game.setRoundMrX(false);

        reachable = Points.getPoints()[31];
    }

    @Test
    public void testIsValidMoveValid(){
        int result = player.isValidMove(game, reachable);
        assertEquals(0, result);
    }

    @Test
    public void testIsValidMoveGameEnd(){
        game.setRound(Game.getNumRounds()+1);
        int result = player.isValidMove(game, reachable);
        assertEquals(1, result);
    }

    @Test
    public void testIsValidMoveNotActive(){
        player.setActive(false);
        int result = player.isValidMove(game, reachable);
        assertEquals(2, result);
    }

    @Test
    public void testIsValidMoveAlreadyMoved(){
        player.setMoved(true);
        int result = player.isValidMove(game, reachable);
        assertEquals(3, result);
    }

    @Test
    public void testIsValidMoveMrXRoundNotMrX(){
        game.setRoundMrX(true);
        player.setMrX(false);

        int result = player.isValidMove(game, reachable);
        assertEquals(3, result);
    }

    @Test
    public void testIsValidMoveMrXNotMrXRound(){
        game.setRoundMrX(false);
        player.setMrX(true);

        int result = player.isValidMove(game, reachable);
        assertEquals(3, result);
    }

    @Test
    public void testIsValidMoveNotReachable(){
        Point newLocation = Points.getPoints()[5];
        int result = player.isValidMove(game, newLocation);
        assertEquals(4, result);
    }

    @Test
    public void testIsValidMoveNoBicycle(){
        player.setPenalty(1);
        int result = player.isValidMove(game, reachable);
        assertEquals(5, result);
    }

    @Test
    public void testIsValidMoveNotEnoughTickets(){
        int result = player2.isValidMove(game, reachable);
        assertEquals(6, result);
    }

    @After
    public void tearDown(){
        player = null;
    }
}
