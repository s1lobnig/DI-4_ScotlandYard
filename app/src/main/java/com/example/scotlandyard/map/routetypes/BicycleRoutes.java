package com.example.scotlandyard.map.routetypes;

import com.example.scotlandyard.map.Point;
import com.example.scotlandyard.map.Route;

public class BicycleRoutes {

    private BicycleRoutes() {

    }

    private static final Point[] INTERMEDIATES_BY_BICYCLE_1_4 = new Point[]{
            new Point(46.621189, 14.262312),
            new Point(46.621058, 14.262452),
            new Point(46.620749, 14.262511),
    };
    private static final Point[] INTERMEDIATES_BY_BICYCLE_4_19 = new Point[]{
            new Point(46.619862, 14.262542),
            new Point(46.620928, 14.262879),
    };
    private static final Point[] INTERMEDIATES_BY_BICYCLE_17_20 = new Point[]{
            new Point(46.620304, 14.265501),
            new Point(46.620492, 14.265378),
            new Point(46.620611, 14.264690),
    };
    private static final Point[] INTERMEDIATES_BY_BICYCLE_19_21 = new Point[]{
            new Point(46.620742, 14.263269),
            new Point(46.620577, 14.264096),
    };
    private static final Point[] INTERMEDIATES_BY_BICYCLE_15_17 = new Point[]{
            new Point(46.620081, 14.267936),
            new Point(46.620016, 14.266739),
    };
    private static final Point[] INTERMEDIATES_BY_BICYCLE_28_34 = new Point[]{
            new Point(46.618482, 14.267531),
            new Point(46.618584, 14.269375),
    };
    private static final Point[] INTERMEDIATES_BY_BICYCLE_33_34 = new Point[]{
            new Point(46.617783, 14.269390),
    };
    private static final Point[] INTERMEDIATES_BY_BICYCLE_32_70 = new Point[]{
            new Point(46.614210, 14.267336),
    };
    private static final Point[] INTERMEDIATES_BY_BICYCLE_58_65 = new Point[]{
            new Point(46.615799, 14.262405),
            new Point(46.615813, 14.263070),
            new Point(46.615410, 14.263321),
    };
    private static final Point[] INTERMEDIATES_BY_BICYCLE_73_74 = new Point[]{
            new Point(46.614175, 14.261760),
            new Point(46.614060, 14.262921),
            new Point(46.613618, 14.262854),
    };
    private static final Point[] INTERMEDIATES_BY_BICYCLE_43_58 = new Point[]{
            new Point(46.616865, 14.261919),
            new Point(46.615870, 14.261783),
    };
    private static final Point[] INTERMEDIATES_BY_BICYCLE_50_54 = new Point[]{
            new Point(46.616992, 14.264037),
            new Point(46.616836, 14.264306),
            new Point(46.616928, 14.265502),
            new Point(46.617023, 14.266079),
            new Point(46.616871, 14.266269),
    };
    private static final Point[] INTERMEDIATES_BY_BICYCLE_40_42 = new Point[]{
            new Point(46.617548, 14.263660),
    };

    private static final Route[] BICYCLE = {
            new Route(1, 4, INTERMEDIATES_BY_BICYCLE_1_4),
            new Route(4, 19, INTERMEDIATES_BY_BICYCLE_4_19),
            new Route(6, 44),
            new Route(19, 21, INTERMEDIATES_BY_BICYCLE_19_21),
            new Route(17, 21),
            new Route(17, 20, INTERMEDIATES_BY_BICYCLE_17_20),
            new Route(15, 17, INTERMEDIATES_BY_BICYCLE_15_17),
            new Route(15, 16),
            new Route(17, 18),
            new Route(14, 18),
            new Route(11, 28),
            new Route(28, 34, INTERMEDIATES_BY_BICYCLE_28_34),
            new Route(33, 34, INTERMEDIATES_BY_BICYCLE_33_34),
            new Route(29, 30),
            new Route(30, 31),
            new Route(31, 32),
            new Route(31, 33),
            new Route(32, 67),
            new Route(32, 70, INTERMEDIATES_BY_BICYCLE_32_70),
            new Route(67, 70),
            new Route(61, 63),
            new Route(58, 65, INTERMEDIATES_BY_BICYCLE_58_65),
            new Route(59, 65),
            new Route(70, 74),
            new Route(73, 74, INTERMEDIATES_BY_BICYCLE_73_74),
            new Route(43, 58, INTERMEDIATES_BY_BICYCLE_43_58),
            new Route(43, 59),
            new Route(51, 58),
            new Route(50, 54, INTERMEDIATES_BY_BICYCLE_50_54),
            new Route(42, 43),
            new Route(40, 42, INTERMEDIATES_BY_BICYCLE_40_42),
            new Route(39, 40)
    };

    public static Route[] getBicycle() {
        return BICYCLE;
    }

}
