package com.example.scotlandyard.connection;

public interface ConnectionInterface {
    void onGameData(Object game);
    void onMessage(Object message);
    void onFailedConnecting(Endpoint endpoint);
    void onDisconnected(Endpoint endpoint);
    void onFailedAcceptConnection(Endpoint endpoint);
}
