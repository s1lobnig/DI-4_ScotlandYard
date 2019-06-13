package com.example.scotlandyard.control;

import com.example.scotlandyard.reportcheater.CheaterReport;

public interface CheaterReportInterface {
    /**
     * Will be fired on cheater report received from network.
     * @param report - received report
     */
    void onReportReceived(CheaterReport report);
}
