package com.example.scotlandyard.connection;

public enum ConnectionState {
        DISCONNECTED,                   // not doing any network activities
        ADVERTISING,                    // advertising endpoint for connection
        DISCOVERING,                    // searching for advertising endpoints
        CONNECTING,                     // connecting to an endpoint
        CONNECTED                       // connected to an endpoint
}
