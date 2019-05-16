package com.example.scotlandyard.control;

import com.example.scotlandyard.connection.Endpoint;
import com.example.scotlandyard.lobby.Lobby;

public interface LobbyInterface {
    void showConnectionFailed(String endpointName);
    void showDisconnected(String endpointName);
    void showAcceptingFailed(String endpointName);
    void showSendingFailed(Object object);
    void updateLobby(Lobby lobby);
}
