package com.example.scotlandyard.Map.Motions;

import com.example.scotlandyard.Player;
import com.google.android.gms.maps.model.Marker;

import java.io.Serializable;

public class SendMove implements Serializable {
    private Player player;
    private Marker field;

    public SendMove(Player player, Marker field) {
        this.player = player;
        this.field = field;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Marker getField() {
        return field;
    }

    public void setField(Marker field) {
        this.field = field;
    }
}
