package com.example.scotlandyard.messenger;

import android.os.Parcelable;

import java.io.Serializable;

public class Message implements Serializable {

    private String message;
    private String nicknameSender;
    private boolean belongsToCurrentUser; // is this message sent by us?

    /**
     * Constructor for creating messages send in messenger
     * @param message
     * @param nicknameSender
     */
    public Message(String message,String nicknameSender) {
        this.message = message;
        this.nicknameSender = nicknameSender;
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

    public void setBelongsToCurrentUser(boolean belongsToCurrentUser) {
        this.belongsToCurrentUser = belongsToCurrentUser;
    }

    public boolean getBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }

    public String getNickname(){
        return this.nicknameSender;
    }

    @Override
    public String toString() {
        return message;
    }

}

