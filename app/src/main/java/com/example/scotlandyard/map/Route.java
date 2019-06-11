package com.example.scotlandyard.map;

import java.io.Serializable;

public class Route implements Serializable {
    private int startPoint;
    private Point[] intermediates;
    private int endPoint;

    public Route(int startPoint, int endPoint, Point[] intermediates) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.intermediates = intermediates;
    }

    public Route(int startPoint, int endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.intermediates = null;
    }

    public int getStartPoint() {
        return startPoint;
    }

    public int getEndPoint() {
        return endPoint;
    }

    public Point[] getIntermediates() {
        return intermediates;
    }

    @Override
    public String toString() {
        return startPoint + " <---> " + endPoint;
    }

    /**
     * method to retrieve the length of a route
     *
     * @return length of the route
     */
    public double getLength() {
        double distance = 0;
        for (int i = 0; i <= getIntermediates().length; i++) {
            double x1;
            double y1;
            double x2;
            double y2;
            if (i == 0) {
                x1 = Points.POINTS[getStartPoint() - 1].getLatitude();
                y1 = Points.POINTS[getStartPoint() - 1].getLongitude();
            } else {
                x1 = getIntermediates()[i - 1].getLatitude();
                y1 = getIntermediates()[i - 1].getLongitude();
            }
            if (i == getIntermediates().length) {
                x2 = Points.POINTS[getEndPoint() - 1].getLatitude();
                y2 = Points.POINTS[getEndPoint() - 1].getLongitude();
            } else {
                x2 = getIntermediates()[i].getLatitude();
                y2 = getIntermediates()[i].getLongitude();
            }
            distance += Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        }
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof Route) {
            Route r = (Route) o;
            if (r.getStartPoint() == startPoint && r.getEndPoint() == endPoint) {
                if (r.getIntermediates() == null && intermediates == null)
                    return true;
                if (r.getIntermediates() != null && intermediates != null && r.getIntermediates().length == intermediates.length) {
                    for (int i = 0; i < intermediates.length; i++) {
                        if (!(intermediates[i]).equals(r.getIntermediates()[i]))
                            return false;
                    }
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
