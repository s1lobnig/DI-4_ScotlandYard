package com.example.scotlandyard;

public class Point {
    private double x;
    private double y;
    int fieldIcon;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(double x, double y, int fieldIcon) {
        this.x = x;
        this.y = y;
        this.fieldIcon = fieldIcon;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getFieldIcon() {
        return fieldIcon;
    }
}
