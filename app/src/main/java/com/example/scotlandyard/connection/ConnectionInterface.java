package com.example.scotlandyard.connection;

/**
 * basic interface of client or server
 */
public interface ConnectionInterface {
    /**
     * function is called, when game data is received
     * @param game          game data
     */
    void onGameData(Object game);

    /**
     * function is called, when a chat message is received
     * @param message       chat message
     */
    void onMessage(Object message);

    /**
     * function is called, when connecting to an endpoint failed
     * @param endpoint      endpoint, where connection failed
     */
    void onFailedConnecting(Endpoint endpoint);

    /**
     * function is called, when connection to an endpoint is lost
     * @param endpoint      endpoint, where connection is lost
     */
    void onDisconnected(Endpoint endpoint);

    /**
     * function is called, when accepting a connection failed
     * @param endpoint      endpoint, where accept connection failed
     */
    void onFailedAcceptConnection(Endpoint endpoint);
}
