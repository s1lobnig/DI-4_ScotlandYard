package com.example.scotlandyard.map.motions;

import com.example.scotlandyard.map.Point;
import com.example.scotlandyard.map.Route;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class MarkerMovingRoute {
    private int icon;
    private int ticket;
    private Point currentPosition;
    private Point newLocation;
    private Route route;
    private List<LatLng> intermediates;
    private List<Float> timeSlices;
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

    public List<LatLng> getIntermediates() {
        return intermediates;
    }

    public void setIntermediates(List<LatLng> intermediates) {
        this.intermediates = intermediates;
    }

    public List<Float> getTimeSlices() {
        return timeSlices;
    }

    public void setTimeSlices(List<Float> timeSlices) {
        this.timeSlices = timeSlices;
    }

    public LatLng getFinalPosition() {
        return finalPosition;
    }

    public void setFinalPosition(LatLng finalPosition) {
        this.finalPosition = finalPosition;
    }
}
