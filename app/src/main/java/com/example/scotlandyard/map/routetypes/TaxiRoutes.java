package com.example.scotlandyard.map.routetypes;

import com.example.scotlandyard.map.Point;
import com.example.scotlandyard.map.Route;

public class TaxiRoutes {

    private TaxiRoutes() {

    }

    private static final Point[] INTERMEDIATES_BY_TAXI_DRAGAN_32_43 = new Point[]{
            new Point(46.614486, 14.264978),
            new Point(46.615011, 14.264580),
            new Point(46.615232, 14.261994),
            new Point(46.615524, 14.262043),
            new Point(46.615649, 14.261788),
            new Point(46.615849, 14.261754),
            new Point(46.616882, 14.261860),
    };
    private static final Point[] INTERMEDIATES_BY_TAXI_DRAGAN_12_32 = new Point[]{
            new Point(46.619603, 14.271313),
            new Point(46.618663, 14.271178),
            new Point(46.617793, 14.271439),
            new Point(46.617666, 14.271495),
            new Point(46.615892, 14.271379),
            new Point(46.615767, 14.271190),
            new Point(46.615443, 14.269621),
    };
    private static final Point[] INTERMEDIATES_BY_TAXI_DRAGAN_12_34 = new Point[]{
            new Point(46.619516, 14.269606),
            new Point(46.618582, 14.269389),
    };

    private static final Route[] TAXI_DRAGAN = {
            new Route(10, 40),
            new Route(32, 43, INTERMEDIATES_BY_TAXI_DRAGAN_32_43),
            new Route(12, 32, INTERMEDIATES_BY_TAXI_DRAGAN_12_32),
            new Route(12, 34, INTERMEDIATES_BY_TAXI_DRAGAN_12_34)
    };

    public static Route[] getTaxiDragan() {
        return TAXI_DRAGAN;
    }
}
