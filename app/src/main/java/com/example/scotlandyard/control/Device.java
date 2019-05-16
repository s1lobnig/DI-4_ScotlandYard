package com.example.scotlandyard.control;

import android.util.Log;

import com.example.scotlandyard.connection.ConnectionService;
import com.example.scotlandyard.lobby.Game;
import com.example.scotlandyard.lobby.Lobby;
import com.example.scotlandyard.map.motions.Move;
import com.example.scotlandyard.messenger.Message;
import com.google.android.gms.nearby.connection.ConnectionsClient;

import java.util.ArrayList;

public class Device {
    private String logTag = "Device";
    private static Device singleton;
    MessengerInterface messengerObserver;
    GameInterface gameObserver;
    ConnectionService connectionService;
    Lobby lobby;
    Game game;
    ArrayList<Message> messageList;

    Device() {

    }

    public static Device getInstance() {
        if (isSingletonSet()) {
            throw new IllegalStateException("singleton not set");
        }
        return singleton;
    }

    public static Device setServer(String endpointName, ConnectionsClient connectionsClient) {
        if (singleton != null) {
            throw new IllegalStateException("singleton already set");
        }
        singleton = new Server(endpointName, connectionsClient);
        return singleton;
    }

    public static Device setClient(String endpointName, ConnectionsClient connectionsClient) {
        if (singleton != null) {
            throw new IllegalStateException("singleton already set");
        }
        singleton = new Client(endpointName, connectionsClient);
        return singleton;
    }

    /**
     * function for resetting the singleton
     */
    public static void resetInstance() {
        //TODO resetting
        singleton = null;
    }

    /**
     * function for retrieving singleton status
     *
     * @return true, if singlet is set
     */
    public static boolean isSingletonSet() {
        return (singleton != null);
    }

    public void addMessengerObserver(MessengerInterface messengerInterface) throws IllegalStateException {
        if (messengerObserver != null) {
            throw new IllegalStateException("messenger observer already added");
        }
        messengerObserver = messengerInterface;
        Log.d(logTag, "added MessengerInterface");
    }

    public void removeMessengerObserver(ServerLobbyInterface lobbyInterface) {
        messengerObserver = null;
        Log.d(logTag, "removed MessengerInterface");
    }

    public void addGameObserver(GameInterface gameInterface) throws IllegalStateException {
        if (gameObserver != null) {
            throw new IllegalStateException("game observer already added");
        }
        gameObserver = gameInterface;
        Log.d(logTag, "added GameInterface");
    }

    public void removeGameObserver(GameInterface gameInterface) {
        gameObserver = null;
        Log.d(logTag, "removed GameInterface");
    }

    void send(Game game) {
        connectionService.send(game);
    }

    void send(Message message) {
        connectionService.send(message);
    }

    void send(Move move) {
        //TODO check move
        connectionService.send(move);
    }
}
