package com.example.scotlandyard;

import android.graphics.Color;

public class Routes {
    public static final float ROUTE_WIDTH = 15f;
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_5_6 = {
            new Point(46.618745, 14.261462),
            new Point(46.618712, 14.261580),
    };
    private static final Route[] BY_FOOT = {
            new Route(1, 2),
            new Route(2, 3),
            new Route(3, 5),
            new Route(2, 4),
            new Route(4, 6),
            new Route(5, 6, INTERMEDIATES_BY_FOOT_PTS_5_6),
            new Route(6, 7),
            new Route(7, 8),
            new Route(8, 9),
            new Route(9, 10),
            new Route(10, 11),
            new Route(10, 18),
            new Route(10, 38),
            new Route(38, 39),
            new Route(28, 35),
            new Route(28, 29),
            new Route(29, 38),
            new Route(29, 36),
            new Route(29, 45),
            new Route(35, 34)
    }; // Color.YELLOW
    private static final int BY_FOOT_COLOR = Color.YELLOW;

    private static final Route[] BICYCLE = {

    }; // Color.ORANGE
    private static final int BICYCLE_COLOR = Color.YELLOW;

    private static final Route[] BUS = {

    }; // Color.RED
    private static final int BUS_COLOR = Color.YELLOW;

    private static final Route[] TAXI_DRAGAN = {

    }; // Color.BLUE
    private static final int TAXI_DRAGAN_COLOR = Color.YELLOW;

    public static Route[] getBicycleRoutes() {
        return BICYCLE;
    }

    public static int getBicycleColor() {
        return BICYCLE_COLOR;
    }

    public static int getBusColor() {
        return BUS_COLOR;
    }

    public static int getByFootColor() {
        return BY_FOOT_COLOR;
    }

    public static int getTaxiDraganColor() {
        return TAXI_DRAGAN_COLOR;
    }

    public static Route[] getBusRoutes() {
        return BUS;
    }

    public static Route[] getByFootRoutes() {
        return BY_FOOT;
    }

    public static Route[] getTaxiDraganRoutes() {
        return TAXI_DRAGAN;
    }
}
