package com.example.scotlandyard;

public class Point {
    private double latitude;
    private double longitude;

    public Point(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public boolean equals(Point p) {
        if (p.getLatitude() == latitude && p.getLongitude() == longitude)
            return true;
        return false;
    }
}
