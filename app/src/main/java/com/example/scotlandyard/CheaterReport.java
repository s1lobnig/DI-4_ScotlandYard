package com.example.scotlandyard;

import java.io.Serializable;

public class CheaterReport implements Serializable {

    /* Nickname of a reporter. */
    private String reporter;

    /* Used to indicate if report is fake or not. */
    private boolean isFakeReport;

    /* Used only to indicate that MrX has lost. When sent to other player it will end the game with "Detectives Won". */
    private boolean isMrXLost;

    public CheaterReport(String reporter) {
        this.reporter = reporter;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public boolean isFakeReport() {
        return isFakeReport;
    }

    public void setFakeReport(boolean fakeReport) {
        isFakeReport = fakeReport;
    }

    public boolean isMrXLost() {
        return isMrXLost;
    }

    public void setMrXLost(boolean mrXLost) {
        isMrXLost = mrXLost;
    }
}
