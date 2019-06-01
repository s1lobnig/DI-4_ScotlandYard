package com.example.scotlandyard.control;

import com.example.scotlandyard.Player;
import com.example.scotlandyard.connection.Endpoint;
import com.example.scotlandyard.map.motions.Move;
import com.example.scotlandyard.messenger.Message;

/**
 * interface for the game
 */
public interface GameInterface {
    /**
     * function is called, when a move is received
     * @param move      Move which is received
     */
    void updateMove(Move move);

    /**
     * function is called, when an endpoint has disconnectd
     * @param endpoint          disconnected endpoint
     */
    void showDisconnected(Endpoint endpoint);

    /**
     * function is called, when sending failed
     * @param object        failed data object
     */
    void showSendingFailed(Object object);

    /**
     * function is called, when chat message is received
     */
    void onMessage();

    /**
     * function is called, when a toast should be printed
     */
    void onReceivedToast(String toast);

    /**
     * function is called, when reconnected
     */
    void showReconnected(String endpointName);

    /**
     * function is called, when reconnecting failed
     */
    void showReconnectFailed(String endpointName);
}
