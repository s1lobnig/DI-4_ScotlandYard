package com.example.scotlandyard.reportcheater;

import android.content.Intent;
import android.widget.Toast;

import com.example.scotlandyard.Player;
import com.example.scotlandyard.R;
import com.example.scotlandyard.gameend.GameEndActivity;
import com.example.scotlandyard.map.GameMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReportingLogic {

    private ReportingLogic() {

    }

    private static int CHEAT_CAUGHT_COUNT = 0;

    public static int getCheatCaughtCount() {
        return CHEAT_CAUGHT_COUNT;
    }

    public static CheaterReport analyseReportMrX(Player player, CheaterReport report) {
        /* If a player is Mr. X. */
        if (player.isMrX()) {
            /* In case that Mr. X has really cheated. */
            if (player.isHasCheated()) {
                /* Counting how many times during the game has Mr. X cheated. */
                CHEAT_CAUGHT_COUNT++;
                /* Reset the variable every time after MrX has cheated and has been reported. */
                player.setHasCheated(false);
                /* If Mr. X has been caught cheating 3 or more times, he shall be punished (by losing the game). */
                if (CHEAT_CAUGHT_COUNT >= 3) {
                    /* Send report-notification to all other players that the game is over and Mr. X has lost.
                    The reports from the players weren't fake (false).
                    Set true - Mr. X lost the game.
                    Send the report to all devices/players. */
                    report.setFakeReport(false);
                    report.setMrXLost(true);
                    return report;
                } else {
                    /* The reports from the players weren't fake (false).
                    Set true - Mr. X lost the game.
                    Send the report to all devices/players. */
                    report.setFakeReport(false);
                    report.setMrXLost(false);
                    return report;
                }
            } else {
                /* In case that Mr. X hasn*t cheated.
                The reports from the players were fake (false).
                Send the report to all devices/players. */
                report.setFakeReport(true);
                return report;
            }
        }
        return null;
    }

    public static int analyzeReportPlayer(Player player, CheaterReport report) {
        if (report.isFakeReport()) {
            if (report.getReporter().equals(player.getNickname())) {
                punishPlayerForFakeReport(player);
                return 0;
            } else {
                return 1;
            }
        } else {
            if (report.isMrXLost()) {
                return 2;
            } else {
                if (report.getReporter().equals(player.getNickname())) {
                    return 3;
                } else {
                    return 4;
                }
            }
        }
    }

    /* This method is used to punish (decrease number of tickets of) this/current player. */
    public static void punishPlayerForFakeReport(Player player) {

        List<Integer> ticketTypes = new ArrayList<>();

        /* Add 4 types of tickets. */
        ticketTypes.add(R.string.PEDESTRIAN_TICKET_KEY);
        ticketTypes.add(R.string.BICYCLE_TICKET_KEY);
        ticketTypes.add(R.string.BUS_TICKET_KEY);

        /* Used to randomize order of ticket types. */
        Collections.shuffle(ticketTypes);

        /* Find at least one ticket which can be removed from player's account (if any). */
        for (int i = 0; i < ticketTypes.size(); i++) {
            if (player.getTickets().get(ticketTypes.get(i)) > 0) {
                player.decreaseNumberOfTickets(ticketTypes.get(i));
                break;
            }
        }
    }

}
