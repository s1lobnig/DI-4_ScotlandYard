package com.example.scotlandyard;

import com.example.scotlandyard.reportcheater.CheaterReport;
import com.example.scotlandyard.reportcheater.ReportingLogic;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;

@RunWith(Parameterized.class)
public class ReportingLogicTest {
    @Parameterized.Parameters
    public static Iterable data() {
        return Arrays.asList(new Object[][]{
                /*
                player
                isMrX
                hasCheated 
                CheaterReport for methods
                CheaterReport sent back from player - analyseReportMrX
                result from player - analyseReportPlayer
                set of tickets, which may be decreased - player
                */
                {
                        new Player("test1"), false, false,
                        new CheaterReport("test1", true, false),
                        null,
                        0,
                        new int[]{R.string.PEDESTRIAN_TICKET_KEY, R.string.BICYCLE_TICKET_KEY, R.string.BUS_TICKET_KEY},
                },
                {
                        new Player("test1"), false, false,
                        new CheaterReport("test2", true, false),
                        null,
                        1,
                        new int[]{R.string.PEDESTRIAN_TICKET_KEY, R.string.BICYCLE_TICKET_KEY, R.string.BUS_TICKET_KEY},
                },
                {
                        new Player("test1"), false, false,
                        new CheaterReport("test1", false, true),
                        null,
                        2,
                        new int[]{R.string.PEDESTRIAN_TICKET_KEY, R.string.BICYCLE_TICKET_KEY, R.string.BUS_TICKET_KEY},
                },
                {
                        new Player("test1"), false, false,
                        new CheaterReport("test1", false, false),
                        null,
                        3,
                        new int[]{R.string.PEDESTRIAN_TICKET_KEY, R.string.BICYCLE_TICKET_KEY, R.string.BUS_TICKET_KEY},
                },
                {
                        new Player("test1"), false, false,
                        new CheaterReport("test2", false, false),
                        null,
                        4,
                        new int[]{R.string.PEDESTRIAN_TICKET_KEY, R.string.BICYCLE_TICKET_KEY, R.string.BUS_TICKET_KEY},
                },
                {
                        new Player("test1"), true, false,
                        new CheaterReport("test2", false, false),
                        new CheaterReport("test2", true, false),
                        -1,
                        new int[]{R.string.PEDESTRIAN_TICKET_KEY, R.string.BICYCLE_TICKET_KEY, R.string.BUS_TICKET_KEY},
                },
                {
                        new Player("test1"), true, true,
                        new CheaterReport("test2", false, false),
                        new CheaterReport("test2", false, false),
                        -1,
                        new int[]{R.string.PEDESTRIAN_TICKET_KEY, R.string.BICYCLE_TICKET_KEY, R.string.BUS_TICKET_KEY},
                },
                {
                        new Player("test1"), true, true,
                        new CheaterReport("test2", false, false),
                        new CheaterReport("test2", false, true),
                        -1,
                        new int[]{R.string.PEDESTRIAN_TICKET_KEY, R.string.BICYCLE_TICKET_KEY, R.string.BUS_TICKET_KEY},
                },
        });
    }

    private Player player;
    private CheaterReport base;
    private CheaterReport report;
    private CheaterReport expectedMrXReport;
    private int expectedPlayerResult;
    private int[] expectedPenalties;

    public ReportingLogicTest(Player player, boolean isMrX, boolean hasCheated,
                              CheaterReport report, CheaterReport expectedMrXReport,
                              int expectedPlayerResult,
                              int[] expectedPenalties) {
        this.player = player;
        this.player.setMrX(isMrX);
        this.player.setHasCheated(hasCheated);
        ArrayList<Player> players = new ArrayList<>();
        players.add(this.player);
        Game game = new Game("TEST", 3, 4, false, false, players);
        this.player.setTickets(game);
        this.base = report;
        this.report = report;
        this.expectedMrXReport = expectedMrXReport;
        this.expectedPlayerResult = expectedPlayerResult;
        this.expectedPenalties = expectedPenalties;
    }

    @Before
    public void setUp() {
        this.report = base;
    }

    @Test
    public void testAnalyseReportMrX() {
        if (expectedMrXReport != null && expectedMrXReport.isMrXLost()) {
            for (int i = 0; i < 3; i++) {
                ReportingLogic.analyseReportMrX(player, report);
                player.setHasCheated(true);
                report = base;
            }
        }
        Assert.assertEquals(expectedMrXReport, ReportingLogic.analyseReportMrX(player, report));
    }

    @Test
    public void testAnalyseReportPlayer() {
        Assert.assertEquals(expectedPlayerResult, ReportingLogic.analyzeReportPlayer(player, report));
    }

    @Test
    public void testPunishPlayerForFakeReport() {
        int t = ReportingLogic.punishPlayerForFakeReport(player);
        boolean isOk = false;
        for (Integer i : expectedPenalties
        ) {
            if (i == t) {
                isOk = true;
                break;
            }
        }
        Assert.assertTrue(isOk);
    }

    @After
    public void tearDown() {
        this.report = null;
    }
}
