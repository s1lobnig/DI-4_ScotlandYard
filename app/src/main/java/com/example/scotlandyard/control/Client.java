package com.example.scotlandyard.control;

import android.util.Log;

import com.example.scotlandyard.Player;
import com.example.scotlandyard.connection.ClientInterface;
import com.example.scotlandyard.connection.ClientService;
import com.example.scotlandyard.connection.Endpoint;
import com.example.scotlandyard.Game;
import com.example.scotlandyard.lobby.Lobby;
import com.example.scotlandyard.map.MapNotification;
import com.example.scotlandyard.map.Points;
import com.example.scotlandyard.map.motions.Move;
import com.example.scotlandyard.map.roadmap.Entry;
import com.example.scotlandyard.messenger.Message;
import com.google.android.gms.nearby.connection.ConnectionsClient;

import java.util.ArrayList;
import java.util.Map;

/**
 * class representing a client in app
 * lobbyObserver:           observer of lobby
 * serverList:              list of servers found
 */
public class Client extends Device implements ClientInterface {
    private String logTag = "Client";
    private ClientLobbyInterface lobbyObserver;
    private ArrayList<Endpoint> serverList;

    Client(String endpointName, ConnectionsClient connectionsClient) {
        connectionService = new ClientService(this, endpointName, connectionsClient);
        serverList = new ArrayList<>();
        messageList = new ArrayList<>();
    }

    /**
     * function for adding lobby observer
     *
     * @param lobbyInterface lobby observer
     * @throws IllegalStateException if already set
     */
    public void addLobbyObserver(ClientLobbyInterface lobbyInterface) throws IllegalStateException {
        if (lobbyObserver != null) {
            throw new IllegalStateException("server lobby observer already added");
        }
        lobbyObserver = lobbyInterface;
        Log.d(logTag, "added ClientLobbyInterface");
    }

    /**
     * function for removing lobby observer
     */
    public void removeLobbyObserver() {
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
            if (!((Message) object).getNickname().equals(nickname)) {
                messageList.add((Message) object);
                if (messengerObserver != null) {
                    messengerObserver.updateMessages(messageList);
                }
            }
            if (gameObserver != null) {
                gameObserver.onMessage();
            }
        }
        if (object instanceof Move) {
            Move move = (Move) object;
            Log.d(logTag, "move received");
            Player player = game.findPlayer(move.getNickname());
            player.setMoved(true);
            if(player.isMrX()){
                game.setRoundMrX(false);
            }

            if (gameObserver != null) {
                gameObserver.updateMove(move);
            }else{
                player.setPosition(Points.POINTS[move.getField()]);
            }
        }
        if (object instanceof Entry) {
            Log.d(logTag, "Roadmap entry received");
            if (!roadMap.getEntries().contains(object))
                roadMap.addEntry((Entry) object);
        }
        if (object instanceof MapNotification) {
            Log.d(logTag, "map notification received");
            manageNotification((MapNotification) object);
        }
        if (object instanceof Lobby) {
            Log.d(logTag, "lobby received");
            if (lobbyObserver != null) {
                lobbyObserver.updateLobby((Lobby) object);
            }
        }
        if (object instanceof Game) {
            Log.d(logTag, "game received");
            game = (Game) object;
            if (lobbyObserver != null) {
                lobbyObserver.startGame((Game) object);
            }
        }
    }

    private void manageNotification(MapNotification notification) {
        String[] txt = notification.getNotification().split(" ");

        if (txt[0].equals("NEXT_ROUND")) {
            game.nextRound();
            game.findPlayer(nickname).setMoved(false);
            game.setRoundMrX(true);
            printNotification("Runde " + game.getRound());
            return;
        }
        if (txt.length == 3 && txt[0].equals("PLAYER") && txt[2].equals("QUITTED")) {
            Player player = game.findPlayer(txt[1]);
            game.deactivatePlayer(player);
            printNotification(txt[1] + " hat das Spiel verlassen");
            return;
        }
        if (txt.length == 2 && txt[0].equals("END")) {
            printNotification(txt[1] + " hat gewonnen");
            return;
        }
        printNotification(notification.getNotification());
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

        if (gameObserver != null) {
            gameObserver.showDisconnected(endpoint);
        }
        if (messengerObserver != null) {
            messengerObserver.showDisconnected(endpoint);
        }
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
        if (gameObserver != null) {
            gameObserver.showSendingFailed(object);
        }
        if (messengerObserver != null) {
            messengerObserver.showSendingFailed(object);
        }
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

    /**
     * function for starting discovery
     */
    public void startDiscovery() {
        Log.d(logTag, "starting discovery");
        ((ClientService) connectionService).startDiscovery();
    }

    /**
     * function for starting discovery
     */
    public void stopDiscovery() {
        Log.d(logTag, "stopping discovery");
        ((ClientService) connectionService).stopDiscovery();
    }

    public ArrayList<Endpoint> getServerList() {
        return serverList;
    }

    /**
     * function for connecting to an endpoint
     *
     * @param position position of endpoint in list
     */
    public void connectToEndpoint(int position) {
        ((ClientService) connectionService).connectToEndpoint(serverList.get(position));
    }
}
