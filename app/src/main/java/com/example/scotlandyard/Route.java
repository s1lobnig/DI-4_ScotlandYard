package com.example.scotlandyard;

import java.util.ArrayList;

public class Route {
    private int start_point;
    private Point[] intermediates;
    private int end_point;

    public Route(int start_point, int end_point) {
        this.start_point = start_point;
        this.end_point = end_point;
        this.intermediates = null;
    }

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
}
