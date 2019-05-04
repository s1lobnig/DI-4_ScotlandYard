package com.example.scotlandyard.messenger;

import java.io.Serializable;

public class Message implements Serializable {

    private String message;
    private boolean belongsToCurrentUser; // is this message sent by us?

    private Message() {
    }

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

