package com.example.scotlandyard.connection;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.Strategy;

/**
 * abstract basic class of the service
 * connectionsClient:       main API class
 * strategy:                network strategy
 * serviceID:               service id of app for identification
 * endpointName             name of local device
 * connectionState          state of the service
 */
abstract public class ConnectionService {
    ConnectionsClient connectionsClient;
    Strategy strategy;
    String serviceID;
    String endpointName;
    ConnectionState connectionState;

    /**
     * Constructor
     * @param endpointName      name of the device (nickname)
     * @param activity          current activity
     */
    ConnectionService(@NonNull String endpointName, @NonNull Activity activity) {
        connectionsClient = Nearby.getConnectionsClient(activity);
        strategy = Strategy.P2P_STAR;
        serviceID = "com.google.aau.scotland_yard";
        this.endpointName = endpointName;
        connectionState = ConnectionState.DISCONNECTED;
    }

    public ConnectionState getConnectionState() {
        return connectionState;
    }
}
