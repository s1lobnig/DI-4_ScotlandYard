package com.example.scotlandyard.control;

import android.util.Log;

import com.example.scotlandyard.reportcheater.CheaterReport;
import com.example.scotlandyard.gameend.GameEnd;
import com.example.scotlandyard.QuitNotification;
import com.example.scotlandyard.connection.ConnectionService;
import com.example.scotlandyard.Game;
import com.example.scotlandyard.lobby.Lobby;
import com.example.scotlandyard.map.MapNotification;
import com.example.scotlandyard.map.motions.Move;
import com.example.scotlandyard.map.roadmap.Entry;
import com.example.scotlandyard.map.roadmap.RoadMap;
import com.example.scotlandyard.messenger.Message;
import com.google.android.gms.nearby.connection.ConnectionsClient;

import java.util.ArrayList;
import java.util.List;

/**
 * class Device is representing a device in the game, it can be server or client
 * singleton:               singleton of device (Server or Client)
 * messengerObserver:       class implementing MessengerInterface. is notified, if message receives
 * gameObserver:            class implementing GameInterface. is notified, if move receives
 * lobby:                   actual lobby
 * game:                    actual game
 * roadMap:                 actual roadMap
 * nickname:                nickname of local player
 * messageList:             message history
 */
public class Device {
    private String logTag = "Device";
    private static Device singleton;
    MessengerInterface messengerObserver;
    GameInterface gameObserver;
    private CheaterReportInterface cheaterReportObserver;
    ConnectionService connectionService;
    Lobby lobby;
    Game game;
    RoadMap roadMap;
    String nickname;
    List<Message> messageList;
    boolean quit;

    Device() {
        messageList = new ArrayList<>();
        roadMap = new RoadMap();
        quit = false;
    }

    /**
     * function for receiving singleton
     *
     * @return singleton of device
     * @throws IllegalStateException if singleton is not set
     */
    public static Device getInstance() {
        if (!isSingletonSet()) {
            throw new IllegalStateException("singleton not set");
        }
        return singleton;
    }

    /**
     * function for setting device as a server
     *
     * @param endpointName      name of local endpoint
     * @param connectionsClient connectionsClient of current Activity
     * @return singleton of device
     * @throws IllegalStateException if singleton is already set
     */
    public static Device setServer(String endpointName, ConnectionsClient connectionsClient) {
        if (singleton != null) {
            throw new IllegalStateException("singleton already set");
        }
        singleton = new Server(endpointName, connectionsClient);
        return singleton;
    }

    /**
     * function for setting device as a client
     *
     * @param endpointName      name of local endpoint
     * @param connectionsClient connectionsClient of current Activity
     * @return singleton of device
     * @throws IllegalStateException if singleton is already set
     */
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

    public static boolean isServer() {
        return (singleton instanceof Server);
    }

    /**
     * function for adding a message observer
     * @param messengerInterface        message observer
     */
    public void addMessengerObserver(MessengerInterface messengerInterface) {
        if (messengerObserver == null) {
            messengerObserver = messengerInterface;
            Log.d(logTag, "added MessengerInterface");
        }
    }

    /**
     * function for removing message observer
     */
    public void removeMessengerObserver() {
        messengerObserver = null;
        Log.d(logTag, "removed MessengerInterface");
    }

    /**
     * function for adding a game observer
     * @param gameInterface             game observer
     */
    public void addGameObserver(GameInterface gameInterface) {
        if (gameObserver == null) {
            gameObserver = gameInterface;
            Log.d(logTag, "added GameInterface");
        }
    }

    /**
     * function for removing game observer
     */
    public void removeGameObserver() {
        gameObserver = null;
        Log.d(logTag, "removed GameInterface");
    }

    /**
     * Function for adding a report observer.
     * @param cheaterReportObserver cheater report observer
     */
    public void setCheaterReportObserver(CheaterReportInterface cheaterReportObserver) {
        this.cheaterReportObserver = cheaterReportObserver;
    }

    /**
     * Function for removing a report observer.
     */
    public void removeCheaterReportObserver() {
        this.cheaterReportObserver = null;
    }

    /**
     * Function for checking if the cheater report observer is set.
     * @return
     */
    public boolean isSetCheaterReportObserver() {
        return this.cheaterReportObserver != null;
    }

    /**
     * Function for returning cheater report observer.
     * @return
     */
    public CheaterReportInterface getCheaterReportObserver() {
        return this.cheaterReportObserver;
    }

    /**
     * function for sending a message
     *
     * @param message message to send
     */
    public void send(Message message) {
        connectionService.send(message);
    }

    /**
     * function for sending a message
     *
     * @param notification map notification to send
     */
    public void send(MapNotification notification) {
        connectionService.send(notification);
    }

    /**
     * function for sending a move
     *
     * @param move move to send
     */
    public void send(Move move) {
        connectionService.send(move);
    }

    /**
     * function for sending a move
     *
     * @param entry move to send
     */
    public void send(Entry entry) {
        connectionService.send(entry);
    }

    public void send(QuitNotification quitNotification) {
        connectionService.send(quitNotification);
    }

    /**
     * function for sending a move
     *
     * @param report report to send
     */
    public void send(CheaterReport report){
        connectionService.send(report);
    }

    /**
     * prints txt on display
     *
     * @param txt text which should be shown as a Toast
     */
    void printNotification(String txt) {
        if (gameObserver != null) {
            gameObserver.onReceivedToast(txt);
        }
        if (messengerObserver != null) {
            messengerObserver.onReceivedToast(txt);
        }
    }

    /**
     * sends control to end the game to device
    */
    public void sendEnd(GameEnd end) {
        connectionService.send(end);
    }

    /**
     * function for sending a move
     */
    public void sendGame() {
        connectionService.send(game);
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public RoadMap getRoadMap() {
        return roadMap;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void addMessage(Message message) {
        messageList.add(message);
    }

    public void setQuit(boolean quit) {
        this.quit = quit;
    }
}
