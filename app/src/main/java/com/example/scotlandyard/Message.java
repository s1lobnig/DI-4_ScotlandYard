package com.example.scotlandyard;

import java.io.Serializable;

public class Message implements Serializable {

    private String message;
    private Player player;
    private boolean belongsToCurrentUser; // is this message sent by us?

    private Message() {
    }

    /**
     * Constructor for creating messages send in messenger
     * @param message
     * @param player
     */
    public Message(String message,Player player) {
        this.message = message;
        this.player = player;
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
        return this.player.getNickname();
    }

}

