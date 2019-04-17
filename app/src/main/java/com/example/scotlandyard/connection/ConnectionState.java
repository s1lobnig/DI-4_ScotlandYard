package com.example.scotlandyard.connection;

/**
 * enum for the state of the ConnectionService
 */
public enum ConnectionState {
        DISCONNECTED,                   // not connected to an endpoint
        ADVERTISING,                    // advertising endpoint for connection
        DISCOVERING,                    // searching for advertising endpoints
        CONNECTING,                     // connecting to an endpoint
        CONNECTED                       // connected to an endpoint
}
