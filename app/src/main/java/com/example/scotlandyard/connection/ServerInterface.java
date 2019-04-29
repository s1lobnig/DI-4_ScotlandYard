package com.example.scotlandyard.connection;

import java.util.Map;

/**
 * interface for server
 */
public interface ServerInterface extends ConnectionInterface {
    /**
     * function is called, when advertising started
     */
    void onStartedAdvertising();

    /**
     * function is called, when advertising failed
     */
    void onFailedAdvertising();

    /**
     * function is called, when advertising stopped
     */
    void onStoppedAdvertising();

    /**
     * function is called, when an endpoint request an connection
     * @param endpoint              endpoint, who requests the connection
     */
    void onConnectionRequested(Endpoint endpoint);

    /**
     * function is called, when an endpoint is connected
     * @param establishedConnections   list of current established connections
     */
    void onConnected(Map<String, Endpoint> establishedConnections);
}