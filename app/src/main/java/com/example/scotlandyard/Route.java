package com.example.scotlandyard;


public class Route {
    private int start_point;
    private Point[] intermediates;
    private int end_point;

    public Route(int start_point, int end_point, Point[] intermediates) {
        this.start_point = start_point;
        this.end_point = end_point;
        this.intermediates = intermediates;
    }

    public int getStart_point() {
        return start_point;
    }

    public int getEnd_point() {
        return end_point;
    }

    public Point[] getIntermediates() {
        return intermediates;
    }

    @Override
    public String toString() {
        return start_point + " <---> " + end_point;
    }

    public double getLength() {
        double distance = 0;
        for (int i = 0; i <= getIntermediates().length; i++) {
            double x1;
            double y1;
            double x2;
            double y2;
            if (i == 0) {
                x1 = Points.POINTS[getStart_point() - 1].getLatitude();
                y1 = Points.POINTS[getStart_point() - 1].getLongitude();
            } else {
                x1 = getIntermediates()[i - 1].getLatitude();
                y1 = getIntermediates()[i - 1].getLongitude();
            }
            if (i == getIntermediates().length) {
                x2 = Points.POINTS[getEnd_point() - 1].getLatitude();
                y2 = Points.POINTS[getEnd_point() - 1].getLongitude();
            } else {
                x2 = getIntermediates()[i].getLatitude();
                y2 = getIntermediates()[i].getLongitude();
            }
            distance += Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        }
        return distance;
    }
}
