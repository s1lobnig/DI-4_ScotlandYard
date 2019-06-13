package com.example.scotlandyard.moving;

import com.example.scotlandyard.Game;
import com.example.scotlandyard.Player;
import com.example.scotlandyard.R;
import com.example.scotlandyard.map.Point;
import com.example.scotlandyard.map.Points;
import com.example.scotlandyard.map.Route;
import com.example.scotlandyard.map.ValidatedRoute;
import com.example.scotlandyard.map.motions.MarkerMovingRoute;
import com.example.scotlandyard.map.motions.MovingLogic;
import com.example.scotlandyard.map.roadmap.Entry;
import com.example.scotlandyard.map.roadmap.PositionEntry;
import com.example.scotlandyard.map.roadmap.TicketEntry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.scotlandyard.map.routetypes.BicycleRoutes.BICYCLE;
import static com.example.scotlandyard.map.routetypes.BusRoutes.BUS;
import static com.example.scotlandyard.map.routetypes.FootRoutes.BY_FOOT;
import static com.example.scotlandyard.map.routetypes.TaxiRoutes.TAXI_DRAGAN;

@RunWith(Parameterized.class)
public class MovingLogicTest {
    @Parameterized.Parameters(name = "{index}: {2} is tested, start = {3}, mrX: {1}, goBack: {20}, randomRoutes: {19}")
    public static Iterable data() {
        return Arrays.asList(new Object[][]{
                /*
                Parameters:
                0...player name
                1...player is mr x
                2...route taken
                3...start point
                4...round
                5...game name
                6...max players
                7...random events enabled
                8...bot enabled
                9...route type
                10..ticket
                11..# intermediates
                12..# time slices
                13..# intermediates GO BACK
                14..# time slices GO BACK
                15..expected Entry
                16..expected icon
                17..expected ticket
                18..expected special move ticket mr x
                19..random routes?
                20..go back?
                21..next point
                22..random route
                23..expected icon prepare
                24..expected ticket prepare
                25..expected current prepare
                26..expected next prepare
                27..expected route prepare
                28..expected icon create
                29..expected ticket create
                30..expected current create
                31..expected next create
                32..expected route create
                33..expected # intermediates create
                34..expected # time slices create
                 */
                // genreal tests
                {"test", false, BY_FOOT[0], Points.FIELDS[0], 1, "TEST", 4, false, false, 0, R.drawable.ticket_yellow, 0, 1, 1, 2, null, R.drawable.pedestrian, R.drawable.ticket_yellow, R.drawable.ticket_yellow, false, false, Points.FIELDS[1], null, R.drawable.pedestrian, R.drawable.ticket_yellow, Points.FIELDS[0], Points.FIELDS[1], BY_FOOT[0], R.drawable.pedestrian, R.drawable.ticket_yellow, Points.FIELDS[0], Points.FIELDS[1], BY_FOOT[0], 0, 1},
                {"test", false, BICYCLE[0], Points.FIELDS[0], 1, "TEST", 4, false, false, 1, R.drawable.ticket_orange, 3, 4, 7, 8, null, R.drawable.bicycle, R.drawable.ticket_orange, R.drawable.ticket_orange, false, false, Points.FIELDS[3], null, R.drawable.bicycle, R.drawable.ticket_orange, Points.FIELDS[0], Points.FIELDS[3], BICYCLE[0], R.drawable.bicycle, R.drawable.ticket_orange, Points.FIELDS[0], Points.FIELDS[3], BICYCLE[0], 3, 4},
                {"test", false, BUS[0], Points.FIELDS[3], 1, "TEST", 4, false, false, 2, R.drawable.ticket_red, 2, 3, 5, 6, null, R.drawable.bus, R.drawable.ticket_red, R.drawable.ticket_red, false, false, Points.FIELDS[15], null, R.drawable.bus, R.drawable.ticket_red, Points.FIELDS[3], Points.FIELDS[15], BUS[0], R.drawable.bus, R.drawable.ticket_red, Points.FIELDS[3], Points.FIELDS[15], BUS[0], 2, 3},
                {"test", false, BY_FOOT[0], Points.FIELDS[1], 1, "TEST", 4, false, false, 0, R.drawable.ticket_yellow, 0, 1, 1, 2, null, R.drawable.pedestrian, R.drawable.ticket_yellow, R.drawable.ticket_yellow, false, false, Points.FIELDS[0], null, R.drawable.pedestrian, R.drawable.ticket_yellow, Points.FIELDS[1], Points.FIELDS[0], BY_FOOT[0], R.drawable.pedestrian, R.drawable.ticket_yellow, Points.FIELDS[1], Points.FIELDS[0], BY_FOOT[0], 0, 1},
                {"test", false, BICYCLE[0], Points.FIELDS[3], 1, "TEST", 4, false, false, 1, R.drawable.ticket_orange, 3, 4, 7, 8, null, R.drawable.bicycle, R.drawable.ticket_orange, R.drawable.ticket_orange, false, false, Points.FIELDS[0], null, R.drawable.bicycle, R.drawable.ticket_orange, Points.FIELDS[3], Points.FIELDS[0], BICYCLE[0], R.drawable.bicycle, R.drawable.ticket_orange, Points.FIELDS[3], Points.FIELDS[0], BICYCLE[0], 3, 4},
                {"test", false, BUS[0], Points.FIELDS[15], 1, "TEST", 4, false, false, 2, R.drawable.ticket_red, 2, 3, 5, 6, null, R.drawable.bus, R.drawable.ticket_red, R.drawable.ticket_red, false, false, Points.FIELDS[3], null, R.drawable.bus, R.drawable.ticket_red, Points.FIELDS[15], Points.FIELDS[3], BUS[0], R.drawable.bus, R.drawable.ticket_red, Points.FIELDS[15], Points.FIELDS[3], BUS[0], 2, 3},
                // Tests for entries and black ticket
                {"test", true, BY_FOOT[0], Points.FIELDS[0], 2, "TEST", 4, false, false, 0, R.drawable.ticket_yellow, 0, 1, 1, 2, new PositionEntry(3, 1), R.drawable.pedestrian, R.drawable.ticket_yellow, R.drawable.ticket_black, false, false, Points.FIELDS[1], null, R.drawable.pedestrian, R.drawable.ticket_black, Points.FIELDS[0], Points.FIELDS[1], BY_FOOT[0], R.drawable.pedestrian, R.drawable.ticket_black, Points.FIELDS[0], Points.FIELDS[1], BY_FOOT[0], 0, 1},
                {"test", true, BY_FOOT[0], Points.FIELDS[0], 4, "TEST", 4, false, false, 0, R.drawable.ticket_yellow, 0, 1, 1, 2, new TicketEntry(5, R.drawable.ticket_yellow), R.drawable.pedestrian, R.drawable.ticket_yellow, R.drawable.ticket_black, false, false, Points.FIELDS[1], null, R.drawable.pedestrian, R.drawable.ticket_black, Points.FIELDS[0], Points.FIELDS[1], BY_FOOT[0], R.drawable.pedestrian, R.drawable.ticket_black, Points.FIELDS[0], Points.FIELDS[1], BY_FOOT[0], 0, 1},
                {"test", true, BY_FOOT[0], Points.FIELDS[0], 6, "TEST", 4, false, false, 0, R.drawable.ticket_yellow, 0, 1, 1, 2, new PositionEntry(7, 1), R.drawable.pedestrian, R.drawable.ticket_yellow, R.drawable.ticket_black, false, false, Points.FIELDS[1], null, R.drawable.pedestrian, R.drawable.ticket_black, Points.FIELDS[0], Points.FIELDS[1], BY_FOOT[0], R.drawable.pedestrian, R.drawable.ticket_black, Points.FIELDS[0], Points.FIELDS[1], BY_FOOT[0], 0, 1},
                {"test", true, BY_FOOT[0], Points.FIELDS[0], 11, "TEST", 4, false, false, 0, R.drawable.ticket_yellow, 0, 1, 1, 2, new PositionEntry(12, 1), R.drawable.pedestrian, R.drawable.ticket_yellow, R.drawable.ticket_black, false, false, Points.FIELDS[1], null, R.drawable.pedestrian, R.drawable.ticket_black, Points.FIELDS[0], Points.FIELDS[1], BY_FOOT[0], R.drawable.pedestrian, R.drawable.ticket_black, Points.FIELDS[0], Points.FIELDS[1], BY_FOOT[0], 0, 1},
                {"test", true, TAXI_DRAGAN[0], Points.FIELDS[9], 11, "TEST", 4, false, false, 3, R.drawable.ticket_blue, 0, 1, 1, 2, new PositionEntry(12, 39), R.drawable.taxi, R.drawable.ticket_blue, R.drawable.ticket_black, false, false, Points.FIELDS[39], null, R.drawable.taxi, R.drawable.ticket_black, Points.FIELDS[9], Points.FIELDS[39], TAXI_DRAGAN[0], R.drawable.taxi, R.drawable.ticket_black, Points.FIELDS[9], Points.FIELDS[39], TAXI_DRAGAN[0], 0, 1},
                // tests for goBack and randomRoute
                {"test", false, BY_FOOT[0], Points.FIELDS[0], 1, "TEST", 4, false, false, 0, R.drawable.ticket_yellow, 0, 1, 1, 2, null, R.drawable.pedestrian, R.drawable.ticket_yellow, R.drawable.ticket_yellow, true, false, Points.FIELDS[1], new ValidatedRoute(BICYCLE[0], true, 1), R.drawable.bicycle, R.drawable.ticket_yellow, Points.FIELDS[0], Points.FIELDS[3], BICYCLE[0], R.drawable.bicycle, R.drawable.ticket_yellow, Points.FIELDS[0], Points.FIELDS[3], BICYCLE[0], 3, 4},
                {"test", false, BY_FOOT[1], Points.FIELDS[3], 1, "TEST", 4, false, false, 0, R.drawable.ticket_yellow, 0, 1, 1, 2, null, R.drawable.pedestrian, R.drawable.ticket_yellow, R.drawable.ticket_yellow, true, false, Points.FIELDS[1], new ValidatedRoute(BICYCLE[0], true, 1), R.drawable.bicycle, R.drawable.ticket_yellow, Points.FIELDS[3], Points.FIELDS[0], BICYCLE[0], R.drawable.bicycle, R.drawable.ticket_yellow, Points.FIELDS[3], Points.FIELDS[0], BICYCLE[0], 3, 4},
                {"test", false, BY_FOOT[1], Points.FIELDS[3], 1, "TEST", 4, false, false, 0, R.drawable.ticket_yellow, 0, 1, 1, 2, null, R.drawable.pedestrian, R.drawable.ticket_yellow, R.drawable.ticket_yellow, true, false, Points.FIELDS[1], new ValidatedRoute(BUS[0], true, 2), R.drawable.bus, R.drawable.ticket_yellow, Points.FIELDS[3], Points.FIELDS[15], BUS[0], R.drawable.bus, R.drawable.ticket_yellow, Points.FIELDS[3], Points.FIELDS[15], BUS[0], 2, 3},
                {"test", false, BICYCLE[0], Points.FIELDS[0], 1, "TEST", 4, false, false, 1, R.drawable.ticket_orange, 3, 4, 7, 8, null, R.drawable.bicycle, R.drawable.ticket_orange, R.drawable.ticket_orange, true, false, Points.FIELDS[3], new ValidatedRoute(BY_FOOT[0], true, 0), R.drawable.pedestrian, R.drawable.ticket_orange, Points.FIELDS[0], Points.FIELDS[1], BY_FOOT[0], R.drawable.pedestrian, R.drawable.ticket_orange, Points.FIELDS[0], Points.FIELDS[1], BY_FOOT[0], 0, 1},
                {"test", true, BY_FOOT[9], Points.FIELDS[9], 1, "TEST", 4, false, false, 0, R.drawable.ticket_yellow, 0, 1, 1, 2, new TicketEntry(2, R.drawable.ticket_yellow), R.drawable.pedestrian, R.drawable.ticket_yellow, R.drawable.ticket_black, true, false, Points.FIELDS[8], new ValidatedRoute(TAXI_DRAGAN[0], true, 3), R.drawable.taxi, R.drawable.ticket_black, Points.FIELDS[9], Points.FIELDS[39], TAXI_DRAGAN[0], R.drawable.taxi, R.drawable.ticket_black, Points.FIELDS[9], Points.FIELDS[39], TAXI_DRAGAN[0], 0, 1},

                {"test", false, BY_FOOT[0], Points.FIELDS[0], 1, "TEST", 4, false, false, 0, R.drawable.ticket_yellow, 0, 1, 1, 2, null, R.drawable.pedestrian, R.drawable.ticket_yellow, R.drawable.ticket_yellow, false, true, Points.FIELDS[1], null, R.drawable.pedestrian, R.drawable.ticket_yellow, Points.FIELDS[0], Points.FIELDS[1], BY_FOOT[0], R.drawable.pedestrian, R.drawable.ticket_yellow, Points.FIELDS[0], Points.FIELDS[0], BY_FOOT[0], 1, 2},
        });
    }

    private Player player;
    private Game game;
    private int round;
    private Point currentPos;
    private Route route;
    private int vehicle;
    private int ticket;
    private int numberOfIntermediates;
    private int numberOfTimeSlices;
    private int numberOfIntermediatesGB;
    private int numberOfTimeSlicesGB;
    private Entry resultingEntry;
    private int resultingIcon;
    private int resultingTicket;
    private int resultingTicketMrX;
    private boolean randomRoute;
    private ValidatedRoute randRoute;
    private boolean goBack;
    private Point next;
    private int expectedIconPM;
    private int expectedTicketPM;
    private Point expectedCurrentPM;
    private Point expectedNewPM;
    private Route expectedRoutePM;
    private int expectedIconCM;
    private int expectedTicketCM;
    private Point expectedCurrentCM;
    private Point expectedNewCM;
    private Route expectedRouteCM;
    private int expectedIntermediatesCM;
    private int expectedTimeSlicesCM;

    public MovingLogicTest(String playerName, boolean isMrX, Route route, Point startPos, int round,
                           String gameName, int maxPlayers, boolean randomEvents, boolean bot,
                           int vehicle, int ticket, int numberOfIntermediates, int numberOfTimeSlices,
                           int numberOfIntermediatesGB, int numberOfTimeSlicesGB,
                           Entry resultingEntry, int resultingIcon,
                           int resultingTicket, int resultingTicketMrX,
                           boolean randomRoute, boolean goBack,
                           Point next, ValidatedRoute randRoute,
                           int expectedIconPM, int expectedTicketPM,
                           Point expectedCurrentPM, Point expectedNewPM,
                           Route expectedRoutePM, int expectedIconCM,
                           int expectedTicketCM, Point expectedCurrentCM,
                           Point expectedNewCM, Route expectedRouteCM,
                           int expectedIntermediatesCM, int expectedTimeSlicesCM) {
        this.player = new Player(playerName);
        this.currentPos = startPos;
        this.player.setMrX(isMrX);
        if (isMrX) {
            this.player.setSpecialMrXMoves(true, 0);
        }
        this.route = route;
        ArrayList<Player> players = new ArrayList<>();
        players.add(this.player);
        players.add(new Player("DUMMY"));
        this.game = new Game(gameName, maxPlayers, round, randomEvents, bot, players);
        this.round = round;
        this.player.setTickets(this.game);
        this.vehicle = vehicle;
        this.ticket = ticket;
        this.resultingTicketMrX = resultingTicketMrX;
        this.numberOfIntermediates = numberOfIntermediates;
        this.numberOfTimeSlices = numberOfTimeSlices;
        this.numberOfIntermediatesGB = numberOfIntermediatesGB;
        this.numberOfTimeSlicesGB = numberOfTimeSlicesGB;
        this.resultingEntry = resultingEntry;
        this.resultingIcon = resultingIcon;
        this.resultingTicket = resultingTicket;
        this.randomRoute = randomRoute;
        this.goBack = goBack;
        this.next = next;
        this.randRoute = randRoute;
        this.expectedIconPM = expectedIconPM;
        this.expectedTicketPM = expectedTicketPM;
        this.expectedCurrentPM = expectedCurrentPM;
        this.expectedNewPM = expectedNewPM;
        this.expectedRoutePM = expectedRoutePM;
        this.expectedIconCM = expectedIconCM;
        this.expectedTicketCM = expectedTicketCM;
        this.expectedCurrentCM = expectedCurrentCM;
        this.expectedNewCM = expectedNewCM;
        this.expectedRouteCM = expectedRouteCM;
        this.expectedIntermediatesCM = expectedIntermediatesCM;
        this.expectedTimeSlicesCM = expectedTimeSlicesCM;
    }

    @Before
    public void setUp() {
        this.player.setPosition(currentPos);
    }

    @Test
    public void testRouteSlicesAndTimings() {
        ArrayList[] result = MovingLogic.getRouteSlicesAndTimings(this.route, Points.getIndex(currentPos) + 1);
        Assert.assertEquals(numberOfIntermediates, result[0].size());
        Assert.assertEquals(numberOfTimeSlices, result[1].size());
    }

    @Test
    public void testGoBackRoute() {
        ArrayList[] result = MovingLogic.getRouteSlicesAndTimings(this.route, Points.getIndex(currentPos) + 1);
        MovingLogic.createGoBackRoute(result[1], result[0], player.getPosition());
        Assert.assertEquals(numberOfIntermediatesGB, result[0].size());
        Assert.assertEquals(numberOfTimeSlicesGB, result[1].size());
    }

    @Test
    public void testIconAndTicket() {
        int[] result = MovingLogic.getIconAndTicket(player, vehicle);
        Assert.assertEquals(resultingIcon, result[0]);
        Assert.assertEquals(resultingTicket, result[1]);
    }

    @Test
    public void testRoadMapEntry() {
        if (player.isMrX()) {
            Assert.assertEquals(resultingEntry, MovingLogic.getRoadMapEntry(this.round, Points.FIELDS[route.getEndPoint() - 1], ticket));
        }
    }

    @Test
    public void testSpecialMrXMoveTicket() {
        Assert.assertEquals(resultingTicketMrX, MovingLogic.getSpecialMrXMoveTicket(player, ticket));
    }

    @Test
    public void testPrepareMove() {
        MarkerMovingRoute markerMovingRoute = MovingLogic.prepareMove(player, randomRoute, randRoute, next);
        Assert.assertEquals(expectedIconPM, markerMovingRoute.getIcon());
        Assert.assertEquals(expectedTicketPM, markerMovingRoute.getTicket());
        Assert.assertEquals(expectedCurrentPM, markerMovingRoute.getCurrentPosition());
        Assert.assertEquals(expectedNewPM, markerMovingRoute.getNewLocation());
        Assert.assertEquals(expectedRoutePM, markerMovingRoute.getRoute());
    }

    @Test
    public void testCreateMove() {
        MarkerMovingRoute markerMovingRoute = MovingLogic.prepareMove(player, randomRoute, randRoute, next);
        markerMovingRoute = MovingLogic.createMove(markerMovingRoute, goBack, player);
        Assert.assertEquals(expectedIconCM, markerMovingRoute.getIcon());
        Assert.assertEquals(expectedTicketCM, markerMovingRoute.getTicket());
        Assert.assertEquals(expectedCurrentCM, markerMovingRoute.getCurrentPosition());
        Assert.assertEquals(expectedNewCM, markerMovingRoute.getNewLocation());
        Assert.assertEquals(expectedRouteCM, markerMovingRoute.getRoute());
        Assert.assertEquals(expectedIntermediatesCM, markerMovingRoute.getIntermediates().size());
        Assert.assertEquals(expectedTimeSlicesCM, markerMovingRoute.getTimeSlices().size());
    }
}
