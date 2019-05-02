package com.example.scotlandyard;

import java.io.Serializable;

public class Message implements Serializable {

    private String message;
    private boolean belongsToCurrentUser; // is this message sent by us?

    private Message() {
    }

    /**
     * Constructor for creating messages send in messenger
     * @param message
     * @param belongsToCurrentUser
     */
    public Message(String message, boolean belongsToCurrentUser) {
        this.message = message;
        this.belongsToCurrentUser = belongsToCurrentUser;
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

}

