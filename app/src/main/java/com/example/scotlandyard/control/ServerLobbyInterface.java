package com.example.scotlandyard.control;

import com.example.scotlandyard.connection.Endpoint;

public interface ServerLobbyInterface extends LobbyInterface {
    void showFailedAdvertising();
    void showStoppedAdvertising();
    void showConnectionRequest(Endpoint endpoint);
}
