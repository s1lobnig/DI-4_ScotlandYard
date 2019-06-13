package com.example.scotlandyard.messenger;


import java.io.Serializable;

public class Message implements Serializable {

    private String messageText;
    private String nicknameSender;
    private boolean belongsToCurrentUser; // is this messageText sent by us?

    /**
     * Constructor for creating messages send in messenger
     * @param messageText
     * @param nicknameSender
     */
    public Message(String messageText, String nicknameSender) {
        this.messageText = messageText;
        this.nicknameSender = nicknameSender;
    }

    /**
     * Constructor for creating messages send in GameList
     * @param messageText
     */
    public Message(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
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
        return messageText;
    }

}

