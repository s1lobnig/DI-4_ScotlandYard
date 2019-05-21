package com.example.scotlandyard.control;

import com.example.scotlandyard.connection.Endpoint;

/**
 * interface for server lobby
 */
public interface ServerLobbyInterface extends LobbyInterface {
    /**
     * function is called, when advertising failed
     */
    void showFailedAdvertising();

    /**
     * function is called, when advertising stopped
     */
    void showStoppedAdvertising();

    /**
     * function is called, if a connection request is received
     * @param endpoint      endpoint, which requests connection
     */
    void showConnectionRequest(Endpoint endpoint);
}
