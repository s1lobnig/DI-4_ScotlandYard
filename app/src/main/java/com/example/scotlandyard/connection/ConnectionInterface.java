package com.example.scotlandyard.connection;

import com.example.scotlandyard.lobby.Game;
import com.example.scotlandyard.map.roadmap.Entry;
import com.example.scotlandyard.messenger.Message;
import com.example.scotlandyard.map.motions.SendMove;

/**
 * basic interface of client or server
 */
public interface ConnectionInterface {
    /**
     * function is called, when data is received
     * @param object    object, which is received
     */
    void onDataReceived(Object object);

    /**
     * function is called, when connecting to an endpoint failed
     *
     * @param endpoint endpoint, where connection failed
     */
    void onFailedConnecting(Endpoint endpoint);

    /**
     * function is called, when connection to an endpoint is lost
     *
     * @param endpoint endpoint, where connection is lost
     */
    void onDisconnected(Endpoint endpoint);

    /**
     * function is called, when accepting a connection failed
     *
     * @param endpoint endpoint, where accept connection failed
     */
    void onFailedAcceptConnection(Endpoint endpoint);

    /**
     * function is called, when sending data failed
     *
     * @param object game data or chat message, which failed
     */
    void onSendingFailed(Object object);

    /**
     * function is called, when connected to an endpoint
     *
     * @param endpoint endpoint with connection to
     */
    void onConnected(Endpoint endpoint);
}
