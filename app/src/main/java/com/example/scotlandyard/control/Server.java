package com.example.scotlandyard.control;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import com.example.scotlandyard.reportcheater.CheaterReport;
import com.example.scotlandyard.gameend.GameEnd;
import com.example.scotlandyard.Player;
import com.example.scotlandyard.QuitNotification;
import com.example.scotlandyard.R;
import com.example.scotlandyard.connection.Endpoint;
import com.example.scotlandyard.connection.ServerInterface;
import com.example.scotlandyard.connection.ServerService;
import com.example.scotlandyard.map.MapNotification;
import com.example.scotlandyard.map.Points;
import com.example.scotlandyard.map.Route;
import com.example.scotlandyard.map.Routes;
import com.example.scotlandyard.map.ValidatedRoute;
import com.example.scotlandyard.map.motions.Move;
import com.example.scotlandyard.map.roadmap.Entry;
import com.example.scotlandyard.messenger.Message;
import com.google.android.gms.nearby.connection.ConnectionsClient;

import java.util.ArrayList;

/**
 * class representing a server in the app
 * lobbyObserver:       observer of lobby
 */
public class Server extends Device implements ServerInterface {
    private String logTag = "Server";
    private ServerLobbyInterface lobbyObserver;
    private ArrayList<Endpoint> lost;
    private ArrayList<String> quited;
    private static final int BOT_MOVE_RANDOM_EVENT_TRIGGER = Integer.MAX_VALUE;

    Server(String endpointName, ConnectionsClient connectionsClient) {
        connectionService = new ServerService(this, endpointName, connectionsClient);
        lost = new ArrayList<>();
        quited = new ArrayList<>();
    }

    /**
     * function for adding lobby observer
     *
     * @param lobbyInterface lobby observer
     * @throws IllegalStateException if already set
     */
    public void addLobbyObserver(ServerLobbyInterface lobbyInterface) {
        if (lobbyObserver == null) {
            lobbyObserver = lobbyInterface;
            Log.d(logTag, "added ServerLobbyInterface");
        }
    }

    /**
     * function for removing lobby observer
     */
    public void removeLobbyObserver() {
        lobbyObserver = null;
        Log.d(logTag, "removed ServerLobbyInterface");
    }

    @Override
    public void onStartedAdvertising() {
        Log.d(logTag, "started advertising");
    }

    @Override
    public void onFailedAdvertising() {
        Log.d(logTag, "failed advertising");
        if (lobbyObserver != null) {
            lobbyObserver.showFailedAdvertising();
        }
        if (!lost.isEmpty()) {
            if (gameObserver != null) {
                gameObserver.showReconnectFailed(lost.get(lost.size() - 1).getName());
            }
            if (messengerObserver != null) {
                messengerObserver.showReconnectFailed(lost.get(lost.size() - 1).getName());
            }
            lost.remove(lost.get(lost.size() - 1));
        }
    }

    @Override
    public void onStoppedAdvertising() {
        Log.d(logTag, "stopped advertising");
        if (lobbyObserver != null) {
            lobbyObserver.showStoppedAdvertising();
        }
    }

    @Override
    public void onConnectionRequested(Endpoint endpoint) {
        Log.d(logTag, endpoint.toString() + " has requested connection.");
        if (lobbyObserver != null) {
            if (lobby.getPlayerCount() < lobby.getMaxPlayers()) {
                if (!lobby.nickAlreadyUsed(endpoint.getName())) {
                    lobbyObserver.showConnectionRequest(endpoint);
                } else {
                    Log.d(logTag, "nick already in use");
                    ((ServerService) connectionService).rejectConnection(endpoint);
                }
            } else {
                Log.d(logTag, "lobby full");
                ((ServerService) connectionService).rejectConnection(endpoint);
                ((ServerService) connectionService).stopAdvertising();
            }
        }
        if (!lost.isEmpty()) {
            if (lost.contains(endpoint)) {
                ((ServerService) connectionService).acceptConnection(endpoint);
            } else {
                ((ServerService) connectionService).rejectConnection(endpoint);
            }
        }
    }

    /**
     * function for accepting connection
     *
     * @param endpoint endpoint to accept
     */
    public void acceptConnection(Endpoint endpoint) {
        ((ServerService) connectionService).acceptConnection(endpoint);
    }

    /**
     * function for rejecting connection
     *
     * @param endpoint endpoint to reject
     */
    public void rejectConnection(Endpoint endpoint) {
        ((ServerService) connectionService).rejectConnection(endpoint);
    }

    public void onDataReceived(Object object, String endpointId) {
        if (object instanceof Message) {
            Log.d(logTag, "message received");
            messageList.add((Message) object);
            connectionService.send(object);
            if (messengerObserver != null) {
                messengerObserver.updateMessages(messageList);
            }
            if (gameObserver != null) {
                gameObserver.onMessage();
            }
        }
        if (object instanceof Move) {
            Log.d(logTag, "move received");
            manageMove((Move) object);
        }
        if (object instanceof Entry) {
            Log.d(logTag, "roadmap entry received");
            Entry entry = (Entry) object;
            roadMap.addEntry(entry);
            if (!game.getPlayers().get(0).isMrX()) {
                roadMap.addEntry(entry);
                send(entry);
            }
        }
        if (object instanceof QuitNotification) {
            onQuitNotification((QuitNotification) object);
        }
        if (object instanceof MapNotification) {
            onMapNotification((MapNotification) object);
        }
        if (object instanceof CheaterReport && isSetCheaterReportObserver()) {
            /* If cheater report observer variable is set, the message will be forwarded to it. */
            getCheaterReportObserver().onReportReceived((CheaterReport) object);
        }
    }

    private void onQuitNotification(QuitNotification quitNotification) {
        Log.d(logTag, "quit received");
        quited.add(quitNotification.getPlayerName());
        disconnect(quitNotification.getPlayerName());
        Player quittedPlayer = game.findPlayer(quitNotification.getPlayerName());
        game.deactivatePlayer(quittedPlayer);
        send(quitNotification);
        if (messengerObserver != null) {
            messengerObserver.onQuit(quitNotification.getPlayerName(), quitNotification.isServerQuit());
        }
        if (gameObserver != null) {
            gameObserver.onQuit(quitNotification.getPlayerName(), quitNotification.isServerQuit());
        }
    }

    private void onMapNotification(MapNotification notification) {
        Log.d(logTag, "map notification received");
        String[] txt = notification.getNotification().split(" ");
        if (txt.length == 2 && txt[1].equals("DEACTIVATED")) {
            Player player = game.findPlayer(txt[0]);
            isNextRound(game.deactivatePlayer(player));
        }
    }

    private void isNextRound(int result) {
        if (result == 1) {
            sendOutNextRound();
        } else if (result == 0 || result == 2) {
            sendOutGameEnd();
        }
    }

    private void manageMove(Move move) {
        Player player = game.findPlayer(move.getNickname());
        send(move);
        if (!player.getSpecialMrXMoves()[1] && !move.isCheatingMove()) {
            player.setMoved(true);
        } else if (player.getSpecialMrXMoves()[1]) {
            player.decreaseNumberOfTickets(R.string.DOUBLE_TICKET_KEY);
            player.setSpecialMrXMoves(false, 1);
        } else {
            player.setMoved(false);
            game.getMrX().decCountCheatingMoves();
        }
        isNextRound(game.tryNextRound());
        /*if(!player.checkAmountOfTickets()){
            String text = player.getNickname() + " wurde deaktiviert.";
            printNotification(text);
            send(new MapNotification(text));
        }*/
        if (gameObserver != null) {
            gameObserver.updateMove(move);
            if (game.checkIfMrxHasLost()) {
                sendEnd(new GameEnd(false));
                quit = true;
                gameObserver.onRecievedEndOfGame(false);
            }
        } else {
            player.setPosition(Points.getFields()[move.getField()]);
        }
    }

    private void sendOutGameEnd() {
        sendEnd(new GameEnd(true));
        quit = true;
        if (gameObserver != null) {
            gameObserver.onRecievedEndOfGame(true);
        }
        disconnect();
        //game has end
        game = null;
    }

    private void sendOutNextRound() {
        send(new MapNotification("NEXT_ROUND"));
        printNotification("Runde " + game.getRound());
        if (game.isRoundMrX() && game.isBotMrX()) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final float d = 4000f;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = elapsed / d;
                    if (t < 1) {
                        handler.postDelayed(this, 16);
                    } else {
                        moveBot();
                    }
                }
            });
        }
    }


    public void moveBot() {
        Player bot = game.getBotMrX();
        bot.setMoved(true);
        int position = Points.getIndex(bot.getPosition()) + 1;
        Route route = Routes.getBotRoute(position, game.getPlayers()).getRoute();

        ValidatedRoute randomRoute = Routes.getRandomRoute(position, route.getEndPoint());
        int newPosition;
        if (route.getEndPoint() == position) {
            newPosition = route.getStartPoint();
        } else {
            newPosition = route.getEndPoint();
        }
        Move move = new Move(bot.getNickname(), newPosition - 1, BOT_MOVE_RANDOM_EVENT_TRIGGER, randomRoute);
        send(move);
        game.setRoundMrX(false);
        if (gameObserver != null) {
            gameObserver.updateMove(move);
        } else {
            bot.setPosition(Points.getFields()[position - 1]);
        }
    }

    @Override
    public void onFailedConnecting(Endpoint endpoint) {
        Log.d(logTag, "connection failed");
        if (lobbyObserver != null) {
            lobbyObserver.showConnectionFailed(endpoint.getName());
        }
        if (!lost.isEmpty()) {
            lost.remove(endpoint);
            if (gameObserver != null) {
                gameObserver.showReconnectFailed(endpoint.getName());
            }
            if (messengerObserver != null) {
                messengerObserver.showReconnectFailed(endpoint.getName());
            }
        }
    }

    @Override
    public void onDisconnected(Endpoint endpoint) {
        Log.d(logTag, "endpoint disconnected");
        //if game has started
        if (game != null) {
            Player lostPlayer = game.findPlayer(endpoint.getName());
            isNextRound(game.deactivatePlayer(lostPlayer));
            send(new MapNotification("PLAYER " + lostPlayer.getNickname() + " LOST"));
            printNotification("Verbindung zu " + lostPlayer.getNickname() + " verloren");
        }
        if (lobbyObserver != null) {
            lobby.removePlayer(endpoint.getName());
            lobbyObserver.updateLobby(lobby);
        }
        if (!quit) {
            if (gameObserver != null) {
                gameObserver.showDisconnected(endpoint);
            }
            if (messengerObserver != null) {
                messengerObserver.showDisconnected(endpoint);
            }
            if (!quited.contains(endpoint.getName())) {
                lost.add(endpoint);
                ((ServerService) connectionService).startAdvertising();
            }
        }
    }

    @Override
    public void onFailedAcceptConnection(Endpoint endpoint) {
        Log.d(logTag, "accepting failed");
        if (lobbyObserver != null) {
            lobbyObserver.showAcceptingFailed(endpoint.getName());
        }
        if (!lost.isEmpty()) {
            lost.remove(endpoint);
            if (gameObserver != null) {
                gameObserver.showReconnectFailed(endpoint.getName());
            }
            if (messengerObserver != null) {
                messengerObserver.showReconnectFailed(endpoint.getName());
            }
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
        Log.d(logTag, "Connection with a new endpoint established.");
        if (lobbyObserver != null) {
            Player newPlayer = new Player(endpoint.getName());
            lobby.addPlayer(newPlayer);
            connectionService.send(lobby);
            lobbyObserver.updateLobby(lobby);
        }
        if (!lost.isEmpty()) {
            lost.remove(endpoint);
            if (gameObserver != null) {
                gameObserver.showReconnected(endpoint.getName());
            }
            if (messengerObserver != null) {
                messengerObserver.showReconnected(endpoint.getName());
            }
            //activate player in game (let him make moves again)
            Player player = game.findPlayer(endpoint.getName());
            player.setActive(true);
            ((ServerService) connectionService).send(this.game, endpoint);
            if (player.isMrX()) {
                game.setBotMrX(false);
            }
        }
    }

    public void startAdvertising() {
        Log.d(logTag, "starting advertising");
        ((ServerService) connectionService).startAdvertising();
    }

    public void stopAdvertising() {
        Log.d(logTag, "stopped advertising");
        ((ServerService) connectionService).stopAdvertising();
    }

    public void disconnect() {
        ((ServerService) connectionService).disconnectFromAll();
        quit = true;
    }

    public void disconnect(String playerName) {
        ((ServerService) connectionService).disconnect(playerName);
        quit = true;
    }
}