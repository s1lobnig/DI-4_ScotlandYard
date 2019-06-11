package com.example.scotlandyard.control;

import com.example.scotlandyard.connection.Endpoint;
import com.example.scotlandyard.Game;

import java.util.ArrayList;

public interface ClientLobbyInterface extends LobbyInterface {
    void showStartedDiscovering();
    void showFailedDiscovering();
    void updateServerList(ArrayList<Endpoint> serverList);
    void showStoppedDiscovery();
    void showConnected(String endpointName);
    void startGame(Game game);
}
