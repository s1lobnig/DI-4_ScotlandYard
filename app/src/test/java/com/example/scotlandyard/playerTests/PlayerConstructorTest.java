package com.example.scotlandyard.playerTests;

import com.example.scotlandyard.Player;
import com.example.scotlandyard.map.Point;
import com.example.scotlandyard.map.Points;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PlayerConstructorTest {
    Player player;

    @Before
    public void setup(){
        player = new Player("Sophie");
    }

    @Test
    public void testConstructor(){
        assertEquals("Sophie", player.getNickname());
        assertTrue(player.isActive());
        assertFalse(player.isMoved());
        assertFalse(player.isMrX());
        assertFalse(player.isHasCheated());
        assertFalse(player.isHasCheatedThisRound());
        assertEquals(0, player.getPenalty());
    }

    @After
    public void tearDown(){
        player = null;
    }
}
