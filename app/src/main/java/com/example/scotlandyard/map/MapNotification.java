package com.example.scotlandyard.map;

import java.io.Serializable;

public class MapNotification implements Serializable {
    private String notification;

    public MapNotification(String notification){
        this.notification = notification;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }
}
