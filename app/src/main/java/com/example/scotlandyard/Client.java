package com.example.scotlandyard;

import com.example.scotlandyard.connection.ClientInterface;
import com.example.scotlandyard.connection.Endpoint;

import java.util.Map;

public class Client implements ClientInterface {
    private static Client singleton = null;


    private Client() {

    }

    /**
     * function for retrieving singleton
     *
     * @return singleton of Client
     * @throws IllegalStateException, if singleton is not set
     */
    public static Client getInstance() throws IllegalStateException {
        if (singleton == null) {
            throw new IllegalStateException("singleton not set");
        }
        return singleton;
    }

    /**
     * function for setting singleton
     *
     * @param clientInterface   object implementing client interface
     * @param endpointName      name of the device (nickname)
     * @param connectionsClient connectionsClient of google api of the activity
     * @return singleton of ClientInterface
     * @throws IllegalStateException, if singleton is already set
     */
    public static Client setInstance() throws IllegalStateException {
        if (singleton != null) {
            throw new IllegalStateException("singleton already set");
        }
        singleton = new Client();
        return singleton;
    }

    /**
     * function for resetting the singleton
     */
    public static void resetInstance() {
        singleton = null;
    }

    @Override
    public void onStartedDiscovery() {

    }

    @Override
    public void onFailedDiscovery() {

    }

    @Override
    public void onEndpointFound(Map<String, Endpoint> discoveredEndpoints) {

    }

    @Override
    public void onEndpointLost(Map<String, Endpoint> discoveredEndpoints) {

    }

    @Override
    public void onStoppedDiscovery() {

    }

    @Override
    public void onDataReceived(Object object) {

    }

    @Override
    public void onFailedConnecting(Endpoint endpoint) {

    }

    @Override
    public void onDisconnected(Endpoint endpoint) {

    }

    @Override
    public void onFailedAcceptConnection(Endpoint endpoint) {

    }

    @Override
    public void onSendingFailed(Object object) {

    }

    @Override
    public void onConnected(Endpoint endpoint) {

    }
}
