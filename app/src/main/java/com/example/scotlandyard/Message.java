package com.example.scotlandyard;

import java.io.Serializable;

public class Message implements Serializable {

    private String message;
    private Player sender;
    private boolean belongsToCurrentUser; // is this message sent by us?

    private Message() {
    }

    /**
     * Constructor for creating messages send in messenger
     * @param message
     * @param sender
     */
    public Message(String message, Player sender) {
        this.message = message;
        this.sender = sender;
    }

    /**
     * Constructor for creating messages send in GameList
     * @param message
     */
    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }

    public Player getSender() {
        return sender;
    }
}

