package com.example.scotlandyard.connection;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.Strategy;

abstract public class ConnectionService {
    ConnectionsClient connectionsClient;
    Strategy strategy;
    String serviceID;
    String endpointName;
    ConnectionState connectionState;

    ConnectionService(@NonNull String endpointName, @NonNull Activity activity) {
        connectionsClient = Nearby.getConnectionsClient(activity);
        strategy = Strategy.P2P_STAR;
        serviceID = "com.google.aau.scotland_yard";
        this.endpointName = endpointName;
        connectionState = ConnectionState.DISCONNECTED;
    }

}
