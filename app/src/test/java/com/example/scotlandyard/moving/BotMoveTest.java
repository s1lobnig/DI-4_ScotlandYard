package com.example.scotlandyard.moving;

import com.example.scotlandyard.Player;
import com.example.scotlandyard.map.Points;
import com.example.scotlandyard.map.Route;
import com.example.scotlandyard.map.Routes;
import com.example.scotlandyard.map.ValidatedRoute;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static com.example.scotlandyard.map.routefactories.BicycleRoutes.BICYCLE;
import static com.example.scotlandyard.map.routefactories.BusRoutes.BUS;
import static com.example.scotlandyard.map.routefactories.FootRoutes.BY_FOOT;
import static com.example.scotlandyard.map.routefactories.TaxiRoutes.TAXI_DRAGAN;

@RunWith(Parameterized.class)
public class BotMoveTest {
    @Parameterized.Parameters(name = "{index}: Route from {0} to {1} is type {2}, valid - {3}")
    public static Iterable data() {
        return Arrays.asList(new Object[][]{
                /*
                bot position
                player positions
                possible resulting routes
                */
                {1,     new int[]{4, 5},            Arrays.asList(new Route[]{BY_FOOT[0]})},
                {24,    new int[]{25, 22, 21, 17},  Arrays.asList(new Route[]{BY_FOOT[25], BY_FOOT[26], BY_FOOT[27]})},
                {
                        1, new int[]{4, 5},
                        Arrays.asList(new Route[]{
                                BY_FOOT[0]
                        })
                },
                {
                        1, new int[]{4, 5},
                        Arrays.asList(new Route[]{
                                BY_FOOT[0]
                        })
                },
                {
                        1, new int[]{4, 5},
                        Arrays.asList(new Route[]{
                                BY_FOOT[0]
                        })
                },
                {
                        1, new int[]{4, 5},
                        Arrays.asList(new Route[]{
                                BY_FOOT[0]
                        })
                }
        });
    }

    private int currentPosition;
    private List<Route> possibleResults;
    private List<Player> players;
    private ValidatedRoute result;

    public BotMoveTest(int currentPosition, int[] playerPositions, List<Route> possibleResults) {
        this.currentPosition = currentPosition;
        this.players = createPlayers(playerPositions);
        this.possibleResults = possibleResults;
    }

    private List<Player> createPlayers(int[] playerPositions) {
        Player[] players = new Player[playerPositions.length];
        for (int i = 0; i < playerPositions.length; i++) {
            players[i] = new Player("" + i);
            players[i].setPosition(Points.POINTS[playerPositions[i] - 1]);
        }
        return Arrays.asList(players);
    }

    @Before
    public void setUp() {
        result = Routes.getBotRoute(currentPosition, players);
    }

    @Test
    public void testIfInPossibleRouteSet() {
        Assert.assertTrue(possibleResults.contains(result.getRoute()));
    }

    @After
    public void tearDown() {
        result = null;
    }
}
