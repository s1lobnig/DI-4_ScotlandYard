package com.example.scotlandyard.control;

import com.example.scotlandyard.connection.Endpoint;
import com.example.scotlandyard.messenger.Message;

import java.util.ArrayList;

/**
 * interface for a Messenger
 */
public interface MessengerInterface {
    /**
     * function is called, when a new message is received
     * @param messages          all messages, including new one
     */
    void updateMessages(ArrayList<Message> messages);

    /**
     * function is called, when an endpoint has disconnected
     * @param endpoint          disconnected endpoint
     */
    void showDisconnected(Endpoint endpoint);

    /**
     * function is called, when sending failed
     * @param object        failed data object
     */
    void showSendingFailed(Object object);

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
