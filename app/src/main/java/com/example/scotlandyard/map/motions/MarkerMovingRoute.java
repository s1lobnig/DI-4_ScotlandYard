package com.example.scotlandyard.map.motions;

import com.example.scotlandyard.map.Point;
import com.example.scotlandyard.map.Route;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MarkerMovingRoute {
    private int icon;
    private int ticket;
    private Point currentPosition;
    private Point newLocation;
    private Route route;
    private ArrayList<LatLng> intermediates;
    private ArrayList<Float> timeSlices;
    private LatLng finalPosition;

    public int getTicket() {
        return ticket;
    }

    public void setTicket(int ticket) {
        this.ticket = ticket;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public MarkerMovingRoute(int icon, int ticket, Point currentPosition, Point newLocation, Route route) {
        this.icon = icon;
        this.ticket = ticket;
        this.currentPosition = currentPosition;
        this.newLocation = newLocation;
        this.route = route;
    }

    public Point getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Point currentPosition) {
        this.currentPosition = currentPosition;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Point getNewLocation() {
        return newLocation;
    }

    public void setNewLocation(Point newLocation) {
        this.newLocation = newLocation;
    }

    public ArrayList<LatLng> getIntermediates() {
        return intermediates;
    }

    public void setIntermediates(ArrayList<LatLng> intermediates) {
        this.intermediates = intermediates;
    }

    public ArrayList<Float> getTimeSlices() {
        return timeSlices;
    }

    public void setTimeSlices(ArrayList<Float> timeSlices) {
        this.timeSlices = timeSlices;
    }

    public LatLng getFinalPosition() {
        return finalPosition;
    }

    public void setFinalPosition(LatLng finalPosition) {
        this.finalPosition = finalPosition;
    }
}
