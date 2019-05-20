package com.example.scotlandyard.control;

import com.example.scotlandyard.lobby.Lobby;

/**
 * basic interface of a lobby
 */
public interface LobbyInterface {
    /**
     * function is called, when connection has failed
     * @param endpointName      name of endpoint
     */
    void showConnectionFailed(String endpointName);

    /**
     * function is called, when disconnected from endpoint
     * @param endpointName      name of endpoint
     */
    void showDisconnected(String endpointName);

    /**
     * function is called, when accepting has failed
     * @param endpointName      name of endpoint
     */
    void showAcceptingFailed(String endpointName);

    /**
     * function is called, when sending has failed
     * @param object            data, which failed
     */
    void showSendingFailed(Object object);

    /**
     * function is called, when the lobby needs to be updated
     * @param lobby             new lobby data
     */
    void updateLobby(Lobby lobby);
}
