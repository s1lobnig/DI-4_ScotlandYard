package com.example.scotlandyard.map;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Point implements Serializable {
    private double latitude;
    private double longitude;
    private int icon;

    public Point(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Point(double latitude, double longitude, int icon) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.icon = icon;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }

    public int getIcon() {
        return icon;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Point) {
            Point p = (Point) o;
            return p.getLatitude() == latitude && p.getLongitude() == longitude;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return latitude + " / " + longitude;
    }
}
