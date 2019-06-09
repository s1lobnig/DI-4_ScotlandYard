package com.example.scotlandyard.map.routefactories;

import com.example.scotlandyard.map.Point;
import com.example.scotlandyard.map.Route;

public class BusRoutes {

    private static final Point[] INTERMEDIATES_BY_BUS_4_16 = new Point[]{
            new Point(46.621232, 14.262722),
            new Point(46.620601, 14.266007)
    };
    private static final Point[] INTERMEDIATES_BY_BUS_12_16 = new Point[]{
            new Point(46.619840, 14.270301),
            new Point(46.620541, 14.266330)
    };
    private static final Point[] INTERMEDIATES_BY_BUS_11_46 = new Point[]{
            new Point(46.619070, 14.267021),
            new Point(46.617869, 14.267365),
            new Point(46.617807, 14.267349),
            new Point(46.617297, 14.267459),
            new Point(46.617089, 14.267090)
    };
    private static final Point[] INTERMEDIATES_BY_BUS_11_41 = new Point[]{
            new Point(46.619070, 14.267021),
            new Point(46.617869, 14.267365),
            new Point(46.617807, 14.267349)
    };
    private static final Point[] INTERMEDIATES_BY_BUS_31_46 = new Point[]{
            new Point(46.615255, 14.268126),
            new Point(46.616488, 14.267723),
            new Point(46.616479, 14.267267)
    };
    private static final Point[] INTERMEDIATES_BY_BUS_41_46 = new Point[]{
            new Point(46.617807, 14.267349),
            new Point(46.617297, 14.267459),
            new Point(46.617089, 14.267090)
    };
    private static final Point[] INTERMEDIATES_BY_BUS_59_74 = new Point[]{
            new Point(46.615429, 14.261393),
            new Point(46.613668, 14.261197)
    };
    private static final Point[] INTERMEDIATES_BY_BUS_41_59 = new Point[]{
            new Point(46.617287, 14.261632),
            new Point(46.615830, 14.261442)
    };
    private static final Point[] INTERMEDIATES_BY_BUS_4_41 = new Point[]{
            new Point(46.619707, 14.262398),
            new Point(46.617287, 14.261632)
    };
    private static final Point[] INTERMEDIATES_BY_BUS_4_59 = new Point[]{
            new Point(46.619707, 14.262398),
            new Point(46.617287, 14.261632),
            new Point(46.615830, 14.261442)
    };

    public static final Route[] BUS = {
            new Route(4, 16, INTERMEDIATES_BY_BUS_4_16),
            new Route(12, 16, INTERMEDIATES_BY_BUS_12_16),
            new Route(11, 12),
            new Route(11, 46, INTERMEDIATES_BY_BUS_11_46),
            new Route(11, 41, INTERMEDIATES_BY_BUS_11_41),
            new Route(41, 46, INTERMEDIATES_BY_BUS_41_46),
            new Route(31, 46, INTERMEDIATES_BY_BUS_31_46),
            new Route(4, 59, INTERMEDIATES_BY_BUS_4_59),
            new Route(59, 74, INTERMEDIATES_BY_BUS_59_74),
            new Route(41, 59, INTERMEDIATES_BY_BUS_41_59),
            new Route(4, 41, INTERMEDIATES_BY_BUS_4_41)
    };

}
