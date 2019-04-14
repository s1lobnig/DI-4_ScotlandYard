package com.example.scotlandyard.connection;

import java.util.Map;

public interface ClientInterface extends ConnectionInterface {
    void onStartedDiscovery();
    void onFailedDiscovery();
    void onEndpointFound(Map<String, Endpoint> discoveredEndpoints);
    void onEndpointLost(Map<String, Endpoint> discoveredEndpoints);
    void onStoppedDiscovery();
    void onConnected();
    void onDisconnected();
}
