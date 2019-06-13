package com.example.scotlandyard.moving;

import com.example.scotlandyard.Player;
import com.example.scotlandyard.map.Point;
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

import static com.example.scotlandyard.map.Points.getFields;
import static com.example.scotlandyard.map.routetypes.BicycleRoutes.BICYCLE;
import static com.example.scotlandyard.map.routetypes.BicycleRoutes.getBicycle;
import static com.example.scotlandyard.map.routetypes.BusRoutes.BUS;
import static com.example.scotlandyard.map.routetypes.BusRoutes.getBUS;
import static com.example.scotlandyard.map.routetypes.FootRoutes.BY_FOOT;
import static com.example.scotlandyard.map.routetypes.FootRoutes.getByFoot;
import static com.example.scotlandyard.map.routetypes.TaxiRoutes.TAXI_DRAGAN;
import static com.example.scotlandyard.map.routetypes.TaxiRoutes.getTaxiDragan;

@RunWith(Parameterized.class)
public class BotMoveTest {

    private static final Route[] BY_FOOT = getByFoot();
    private static final Route[] BICYCLE = getBicycle();
    private static final Route[] BUS = getBUS();
    private static final Route[] TAXI_DRAGAN = getTaxiDragan();
    private static final Point[] FIELDS = getFields();

    @Parameterized.Parameters(name = "{index}: Bot at {0}, other players at {1}")
    public static Iterable data() {
        return Arrays.asList(new Object[][]{
                /*
                bot position
                player positions
                possible resulting routes
                */
                {1, new int[]{4, 5}, Arrays.asList(new Route[]{BY_FOOT[0]})},
                {24, new int[]{25, 22, 21, 17}, Arrays.asList(new Route[]{BY_FOOT[25], BY_FOOT[26], BY_FOOT[27]})},
                {32, new int[]{12, 43, 66}, Arrays.asList(new Route[]{BICYCLE[15], BICYCLE[17], BICYCLE[18],})},
                {32, new int[]{31, 66, 67, 70}, Arrays.asList(new Route[]{TAXI_DRAGAN[1], TAXI_DRAGAN[2]})},
                {4, new int[]{1, 2, 6, 19}, Arrays.asList(new Route[]{BUS[0], BUS[7], BUS[10]})},
                {1, new int[]{2, 4}, Arrays.asList(new Route[]{BY_FOOT[0], BICYCLE[0]})},

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
        Player[] players = new Player[playerPositions.length + 1];
        for (int i = 0; i < playerPositions.length; i++) {
            players[i] = new Player("" + i);
            players[i].setPosition(FIELDS[playerPositions[i] - 1]);
        }
        players[players.length - 1] = new Player("BOT");
        players[players.length - 1].setPosition(FIELDS[currentPosition - 1]);
        players[players.length - 1].setMrX(true);
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
