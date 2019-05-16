package com.example.scotlandyard.control;

import android.util.Log;

import com.example.scotlandyard.connection.ClientInterface;
import com.example.scotlandyard.connection.ClientService;
import com.example.scotlandyard.connection.Endpoint;
import com.example.scotlandyard.lobby.Game;
import com.example.scotlandyard.lobby.Lobby;
import com.example.scotlandyard.map.motions.Move;
import com.example.scotlandyard.messenger.Message;
import com.google.android.gms.nearby.connection.ConnectionsClient;

import java.util.ArrayList;
import java.util.Map;

public class Client extends Device implements ClientInterface {
    private String logTag = "Client";
    private ClientLobbyInterface lobbyObserver;
    private ArrayList<Endpoint> serverList;

    Client(String endpointName, ConnectionsClient connectionsClient) {
        connectionService = new ClientService(this, endpointName, connectionsClient);
    }

    public void addLobbyObserver(ClientLobbyInterface lobbyInterface) throws IllegalStateException {
        if (lobbyObserver != null) {
            throw new IllegalStateException("server lobby observer already added");
        }
        lobbyObserver = lobbyInterface;
        Log.d(logTag, "added ClientLobbyInterface");
    }

    public void removeLobbyObserver(ClientLobbyInterface lobbyInterface) {
        lobbyObserver = null;
        Log.d(logTag, "removed ClientLobbyInterface");
    }

    @Override
    public void onStartedDiscovery() {
        Log.d(logTag, "started discovering");
        if (lobbyObserver != null) {
            lobbyObserver.showStartedDiscovering();
        }
    }

    @Override
    public void onFailedDiscovery() {
        Log.d(logTag, "failed discovering");
        if (lobbyObserver != null) {
            lobbyObserver.showFailedDiscovering();
        }
    }

    @Override
    public void onEndpointFound(Map<String, Endpoint> discoveredEndpoints) {
        Log.d(logTag, "endpoint discovered");
        serverList = new ArrayList<>(discoveredEndpoints.values());
        if (lobbyObserver != null) {
            lobbyObserver.updateServerList(serverList);
        }
    }

    @Override
    public void onEndpointLost(Map<String, Endpoint> discoveredEndpoints) {
        Log.d(logTag, "endpoint lost");
        serverList = new ArrayList<>(discoveredEndpoints.values());
        if (lobbyObserver != null) {
            lobbyObserver.updateServerList(serverList);
        }
    }

    @Override
    public void onStoppedDiscovery() {
        Log.d(logTag, "stopped discovery");
        if (lobbyObserver != null) {
            lobbyObserver.showStoppedDiscovery();
        }
    }

    @Override
    public void onDataReceived(Object object, String endpointId) {
        if (object instanceof Message) {
            Log.d(logTag, "message received");
            messageList.add((Message)object);
            if (messengerObserver != null) {
                messengerObserver.updateMessages(messageList);
            }
        }
        if (object instanceof Move) {
            if (game.doMove((Move)object)) {
                Log.d(logTag, "move received");
                if (gameObserver != null) {
                    gameObserver.updateMove((Move)object);
                }
            } else {
                Log.d(logTag, "not a valid move");
            }
        }
        if (object instanceof Lobby) {
            Log.d(logTag, "lobby received");
            if (lobbyObserver != null) {
                lobbyObserver.updateLobby((Lobby)object);
            }
        }
        if (object instanceof Game) {
            Log.d(logTag, "game received");
            if (lobbyObserver != null) {
                //TODO start game
            }
        }
    }

    @Override
    public void onFailedConnecting(Endpoint endpoint) {
        Log.d(logTag, "failed connecting");
        if (lobbyObserver != null) {
            lobbyObserver.showConnectionFailed(endpoint.getName());
        }
    }

    @Override
    public void onDisconnected(Endpoint endpoint) {
        Log.d(logTag, "disconnected");
        //TODO other observers
        if (lobbyObserver != null) {
            lobbyObserver.showDisconnected(endpoint.getName());
        }
    }

    @Override
    public void onFailedAcceptConnection(Endpoint endpoint) {
        Log.d(logTag, "accepting failed");
        if (lobbyObserver != null) {
            lobbyObserver.showAcceptingFailed(endpoint.getName());
        }
    }

    @Override
    public void onSendingFailed(Object object) {
        Log.d(logTag, "sending failed");
        //TODO other observers
        if (lobbyObserver != null) {
            lobbyObserver.showSendingFailed(object);
        }
    }

    @Override
    public void onConnected(Endpoint endpoint) {
        Log.d(logTag, "connected");
        if (lobbyObserver != null) {
            lobbyObserver.showConnected(endpoint.getName());
        }
    }

    public void startDiscovery() {
        Log.d(logTag, "starting discovery");
        ((ClientService)connectionService).startDiscovery();
    }

    public void stopDiscovery() {
        Log.d(logTag, "stopped discovery");
        ((ClientService)connectionService).stopDiscovery();
    }

    public ArrayList<Endpoint> getServerList() {
        return serverList;
    }
}
