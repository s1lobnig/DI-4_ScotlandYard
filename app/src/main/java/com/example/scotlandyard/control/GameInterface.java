package com.example.scotlandyard.control;

import com.example.scotlandyard.Player;
import com.example.scotlandyard.connection.Endpoint;
import com.example.scotlandyard.map.motions.Move;

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
     * @param player    Player who has quitted and must be removed
     */
    void removePlayer(Player player);

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
}