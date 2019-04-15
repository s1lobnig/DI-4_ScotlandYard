package com.example.scotlandyard.connection;

import java.util.Map;

/**
 * interface for client
 */
public interface ClientInterface extends ConnectionInterface {
    /**
     * function is called, when discovery started
     */
    void onStartedDiscovery();

    /**
     * function is called, when discovery failed
     */
    void onFailedDiscovery();

    /**
     * function is called, when an endpoint is found
     * @param discoveredEndpoints       list of current discovered endpoints
     */
    void onEndpointFound(Map<String, Endpoint> discoveredEndpoints);

    /**
     * function is called, when an endpoint is lost
     * @param discoveredEndpoints       list of current discovered endpoints
     */
    void onEndpointLost(Map<String, Endpoint> discoveredEndpoints);

    /**
     * function is called, when discovery stopped
     */
    void onStoppedDiscovery();

    /**
     * function is called, when connected to an endpoint
     * @param endpoint                  endpoint with connection to
     */
    void onConnected(Endpoint endpoint);
}
