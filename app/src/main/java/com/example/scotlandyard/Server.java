package com.example.scotlandyard;

import com.example.scotlandyard.connection.Endpoint;
import com.example.scotlandyard.connection.ServerInterface;
import com.example.scotlandyard.connection.ServerService;

public class Server implements ServerInterface {
    private String logTag = "Server";
    private static Server singleton = null;
    private ServerService serverService;


    private Server() {

    }

    /**
     * function for retrieving singleton
     *
     * @return singleton of Server
     * @throws IllegalStateException, if singleton is not set
     */
    public static Server getInstance() throws IllegalStateException {
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
    public static Server setInstance() throws IllegalStateException {
        if (singleton != null) {
            throw new IllegalStateException("singleton already set");
        }
        singleton = new Server();
        return singleton;
    }

    /**
     * function for resetting the singleton
     */
    public static void resetInstance() {
        singleton = null;
    }

    /**
     * function for retrieving singleton status
     *
     * @return true, if singlet is set
     */
    public static boolean isSingletonSet() {
        return (singleton != null);
    }

    @Override
    public void onStartedAdvertising() {

    }

    @Override
    public void onFailedAdvertising() {

    }

    @Override
    public void onStoppedAdvertising() {

    }

    @Override
    public void onConnectionRequested(Endpoint endpoint) {

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
