package com.example.scotlandyard;

import java.io.Serializable;

public class CheaterReport implements Serializable {

    private String reporter;
    private boolean isFake;

    public CheaterReport(String reporter) {
        this.reporter = reporter;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public boolean isFake() {
        return isFake;
    }

    public void setFake(boolean fake) {
        isFake = fake;
    }
}