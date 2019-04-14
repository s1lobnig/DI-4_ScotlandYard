package com.example.scotlandyard.connection;

import java.util.Map;

public interface ServerInterface extends ConnectionInterface {
    void onStartedAdvertising();
    void onFailedAdvertising();
    void onStoppedAdvertising();
    void onConnectionRequested(Endpoint endpoint);
    void onConnected(Map<String, Endpoint> discoveredEndpoints);
}
