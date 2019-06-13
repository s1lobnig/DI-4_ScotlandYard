package com.example.scotlandyard.reportcheater;

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
        isFakeReport = false;
        isMrXLost = false;
    }

    public CheaterReport(String reporter, boolean isFakeReport, boolean isMrXLost) {
        this.reporter = reporter;
        this.isFakeReport = isFakeReport;
        this.isMrXLost = isMrXLost;
    }

    public String getReporter() {
        return reporter;
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CheaterReport) {
            CheaterReport c = (CheaterReport) obj;
            return this.reporter.equals(c.reporter) && this.isFakeReport == c.isFakeReport && this.isMrXLost == c.isMrXLost;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return reporter.hashCode();
    }
}
