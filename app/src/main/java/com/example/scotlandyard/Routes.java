package com.example.scotlandyard;

import android.graphics.Color;

public class Routes {
    public static final float ROUTE_WIDTH = 15f;
    // 5 - 6
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_5_6 = {
            new Point(46.618745, 14.261462),
            new Point(46.618712, 14.261580),
    };
    // 20 - 21
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_20_21 = {
            new Point(46.620758, 14.264536),
            new Point(46.620544, 14.264391)
    };
    // 21 - 22
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_21_22 = {
            new Point(46.620322, 14.264098),
            new Point(46.620281, 14.263773),
            new Point(46.620164, 14.263646)
    };
    // 21 - 23
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_21_23 = {
            new Point(46.620322, 14.264098),
            new Point(46.620149, 14.264186),
            new Point(46.620059, 14.264390),
            new Point(46.619938, 14.264483)
    };
    // 21 - 24
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_21_24 = {
            new Point(46.620322, 14.264098),
            new Point(46.620252, 14.264625),
            new Point(46.619966, 14.265080)
    };
    // 22 - 23
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_22_23 = {
            new Point(46.619799, 14.263644),
            new Point(46.619712, 14.263757),
            new Point(46.619694, 14.264272),
            new Point(46.619821, 14.264491)
    };
    // 23 - 24
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_23_24 = {
            new Point(46.620018, 14.264658),
            new Point(46.619990, 14.264842),
            new Point(46.619902, 14.264867)
    };
    // 24 - 27
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_24_27 = {
            new Point(46.619743, 14.265144),
            new Point(46.619589, 14.265078),
            new Point(46.619292, 14.265090),
            new Point(46.619268, 14.265285)
    };
    // 24 - 26
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_24_26 = {
            new Point(46.619624, 14.264825),
            new Point(46.619516, 14.264847),
            new Point(46.619496, 14.264725),
            new Point(46.619354, 14.264780),
            new Point(46.619262, 14.264750)
    };
    // 26 - 27
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_26_27 = {
            new Point(46.619262, 14.264750),
            new Point(46.619233, 14.264938),
            new Point(46.619292, 14.265090),
            new Point(46.619268, 14.265285)
    };
    // 22 - 25
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_22_25 = {
            new Point(46.619693, 14.263541),
            new Point(46.619650, 14.263599),
            new Point(46.619327, 14.263563),
            new Point(46.619357, 14.263670)
    };

    /* TODO: Add missing intermediates for all routes */
    // 25 - 26
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_25_26 = {
            new Point(46.619375, 14.264148)
    };
    // 10 - 38
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_10_38 = {
    };
    // 14 - 15
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_14_15 = {
    };
    // 28 - 35
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_28_35 = {
    };
    // 29 - 35
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_29_35 = {
    };
    // 29 - 36
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_29_36 = {
    };
    // 36 - 37
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_36_37 = {
    };
    // 42 - 51
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_42_51 = {
    };
    // 45 - 46
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_45_46 = {
    };
    // 46 - 47
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_46_47 = {
    };
    // 46 - 53
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_46_53 = {
    };
    // 47 - 48
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_47_48 = {
    };
    // 47 - 54
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_47_54 = {
    };
    // 50 - 51
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_50_51 = {
    };
    // 53 - 61
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_53_61 = {
    };
    // 56 - 62
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_56_62 = {
    };
    // 59 - 73
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_59_73 = {
    };
    // 62 - 63
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_62_63 = {
    };
    // 64 - 65
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_64_65 = {
    };
    // 71 - 74
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_71_74 = {
    };

    private static final Route[] BY_FOOT = {
            new Route(1, 2, null),
            new Route(2, 3, null),
            new Route(2, 4, null),
            new Route(3, 5, null),
            new Route(4, 6, null),
            new Route(5, 6, INTERMEDIATES_BY_FOOT_PTS_5_6),
            new Route(6, 7, null),
            new Route(7, 8, null),
            new Route(8, 9, null),
            new Route(9, 10, null),
            new Route(10, 11, null),
            new Route(10, 18, null),
            new Route(10, 38, INTERMEDIATES_BY_FOOT_PTS_10_38),
            new Route(12, 13, null),
            new Route(13, 14, null),
            new Route(14, 15, INTERMEDIATES_BY_FOOT_PTS_14_15),
            new Route(16, 17, null),
            new Route(17, 24, null),
            new Route(19, 20, null),
            new Route(20, 21, INTERMEDIATES_BY_FOOT_PTS_20_21),
            new Route(21, 24, INTERMEDIATES_BY_FOOT_PTS_21_24),
            new Route(21, 23, INTERMEDIATES_BY_FOOT_PTS_21_23),
            new Route(21, 22, INTERMEDIATES_BY_FOOT_PTS_21_22),
            new Route(22, 23, INTERMEDIATES_BY_FOOT_PTS_22_23),
            new Route(22, 25, INTERMEDIATES_BY_FOOT_PTS_22_25),
            new Route(23, 24, INTERMEDIATES_BY_FOOT_PTS_23_24),
            new Route(24, 27, INTERMEDIATES_BY_FOOT_PTS_24_27),
            new Route(24, 26, INTERMEDIATES_BY_FOOT_PTS_24_26),
            new Route(25, 26, INTERMEDIATES_BY_FOOT_PTS_25_26),
            new Route(26, 27, INTERMEDIATES_BY_FOOT_PTS_26_27),
            new Route(28, 38, null),
            new Route(28, 35, INTERMEDIATES_BY_FOOT_PTS_28_35),
            new Route(28, 29, null),
            new Route(28, 39, null),
            new Route(29, 36, INTERMEDIATES_BY_FOOT_PTS_29_36),
            new Route(29, 35, INTERMEDIATES_BY_FOOT_PTS_29_35),
            new Route(29, 45, null),
            new Route(30, 37, null),
            new Route(30, 53, null),
            new Route(30, 60, null),
            new Route(31, 66, null),
            new Route(32, 66, null),
            new Route(33, 36, null),
            new Route(33, 37, null),
            new Route(34, 35, null),
            new Route(36, 37, INTERMEDIATES_BY_FOOT_PTS_36_37),
            new Route(38, 39, null),
            new Route(40, 41, null),
            new Route(41, 48, null),
            new Route(42, 49, null),
            new Route(42, 50, null),
            new Route(42, 51, INTERMEDIATES_BY_FOOT_PTS_42_51),
            new Route(43, 52, null),
            new Route(43, 44, null),
            new Route(43, 51, null),
            new Route(45, 46, INTERMEDIATES_BY_FOOT_PTS_45_46),
            new Route(46, 47, INTERMEDIATES_BY_FOOT_PTS_46_47),
            new Route(46, 53, INTERMEDIATES_BY_FOOT_PTS_46_53),
            new Route(47, 48, INTERMEDIATES_BY_FOOT_PTS_47_48),
            new Route(47, 54, INTERMEDIATES_BY_FOOT_PTS_47_54),
            new Route(48, 49, null),
            new Route(50, 51, INTERMEDIATES_BY_FOOT_PTS_50_51),
            new Route(51, 52, null),
            new Route(52, 58, null),
            new Route(53, 61, INTERMEDIATES_BY_FOOT_PTS_53_61),
            new Route(54, 55, null),
            new Route(55, 61, null),
            new Route(55, 56, null),
            new Route(56, 62, INTERMEDIATES_BY_FOOT_PTS_56_62),
            new Route(56, 63, null),
            new Route(56, 75, null),
            new Route(57, 64, null),
            new Route(57, 75, null),
            new Route(57, 58, null),
            new Route(58, 59, null),
            new Route(59, 73, INTERMEDIATES_BY_FOOT_PTS_59_73),
            new Route(60, 61, null),
            new Route(60, 66, null),
            new Route(66, 67, null),
            new Route(62, 63, INTERMEDIATES_BY_FOOT_PTS_62_63),
            new Route(63, 64, null),
            new Route(63, 68, null),
            new Route(64, 65, INTERMEDIATES_BY_FOOT_PTS_64_65),
            new Route(65, 69, null),
            new Route(65, 72, null),
            new Route(68, 67, null),
            new Route(68, 69, null),
            new Route(69, 71, null),
            new Route(70, 71, null),
            new Route(71, 72, null),
            new Route(71, 74, INTERMEDIATES_BY_FOOT_PTS_71_74),
            new Route(72, 74, null),
            new Route(72, 73, null)
    }; // Color.YELLOW
    private static final int BY_FOOT_COLOR = Color.YELLOW;

    // 1 - 4
    private static final Point[] INTERMEDIATES_BY_BICYCLE_1_4 = new Point[]{
    };
    // 4 - 19
    private static final Point[] INTERMEDIATES_BY_BICYCLE_4_19 = new Point[]{
    };
    // 17 - 20
    private static final Point[] INTERMEDIATES_BY_BICYCLE_17_20 = new Point[]{
    };
    // 19 - 21
    private static final Point[] INTERMEDIATES_BY_BICYCLE_19_21 = new Point[]{
    };
    // 15 - 17
    private static final Point[] INTERMEDIATES_BY_BICYCLE_15_17 = new Point[]{
    };
    // 28 - 34
    private static final Point[] INTERMEDIATES_BY_BICYCLE_28_34 = new Point[]{
    };
    // 33 - 34
    private static final Point[] INTERMEDIATES_BY_BICYCLE_33_34 = new Point[]{
    };
    // 32 - 70
    private static final Point[] INTERMEDIATES_BY_BICYCLE_32_70 = new Point[]{
    };
    // 58 - 65
    private static final Point[] INTERMEDIATES_BY_BICYCLE_58_65 = new Point[]{
    };
    // 73 - 74
    private static final Point[] INTERMEDIATES_BY_BICYCLE_73_74 = new Point[]{
    };
    // 43 - 58
    private static final Point[] INTERMEDIATES_BY_BICYCLE_43_58 = new Point[]{
    };
    // 51 - 58
    private static final Point[] INTERMEDIATES_BY_BICYCLE_51_58 = new Point[]{
    };
    // 50 - 54
    private static final Point[] INTERMEDIATES_BY_BICYCLE_50_54 = new Point[]{
    };
    // 40 - 42
    private static final Point[] INTERMEDIATES_BY_BICYCLE_40_42 = new Point[]{
    };

    private static final Route[] BICYCLE = {
            new Route(1, 4, INTERMEDIATES_BY_BICYCLE_1_4),
            new Route(4, 19, INTERMEDIATES_BY_BICYCLE_4_19),
            new Route(6, 44, null),
            new Route(19, 21, INTERMEDIATES_BY_BICYCLE_19_21),
            new Route(17, 21, null),
            new Route(17, 20, INTERMEDIATES_BY_BICYCLE_17_20),
            new Route(15, 17, INTERMEDIATES_BY_BICYCLE_15_17),
            new Route(15, 16, null),
            new Route(17, 18, null),
            new Route(14, 18, null),
            new Route(11, 28, null),
            new Route(28, 34, INTERMEDIATES_BY_BICYCLE_28_34),
            new Route(33, 34, INTERMEDIATES_BY_BICYCLE_33_34),
            new Route(29, 30, null),
            new Route(30, 31, null),
            new Route(31, 32, null),
            new Route(31, 33, null),
            new Route(32, 67, null),
            new Route(32, 70, INTERMEDIATES_BY_BICYCLE_32_70),
            new Route(67, 70, null),
            new Route(61, 63, null),
            new Route(58, 65, INTERMEDIATES_BY_BICYCLE_58_65),
            new Route(59, 65, null),
            new Route(70, 74, null),
            new Route(73, 74, INTERMEDIATES_BY_BICYCLE_73_74),
            new Route(43, 58, INTERMEDIATES_BY_BICYCLE_43_58),
            new Route(43, 59, null),
            new Route(51, 58, INTERMEDIATES_BY_BICYCLE_51_58),
            new Route(50, 54, INTERMEDIATES_BY_BICYCLE_50_54),
            new Route(42, 43, null),
            new Route(40, 42, INTERMEDIATES_BY_BICYCLE_40_42),
            new Route(39, 40, null)
    }; // Color.ORANGE
    private static final int BICYCLE_COLOR = Color.rgb(255, 164, 17);

    // 4 - 16
    private static final Point[] INTERMEDIATES_BY_BUS_4_16 = new Point[]{
    };
    // 12 - 16
    private static final Point[] INTERMEDIATES_BY_BUS_12_16 = new Point[]{
    };
    // 11 - 46
    private static final Point[] INTERMEDIATES_BY_BUS_11_46 = new Point[]{
    };
    // 11 - 41
    private static final Point[] INTERMEDIATES_BY_BUS_11_41 = new Point[]{
    };
    // 31 - 46
    private static final Point[] INTERMEDIATES_BY_BUS_31_46 = new Point[]{
    };
    // 41 - 46
    private static final Point[] INTERMEDIATES_BY_BUS_41_46 = new Point[]{
    };
    // 59 - 74
    private static final Point[] INTERMEDIATES_BY_BUS_59_74 = new Point[]{
    };
    // 41 - 59
    private static final Point[] INTERMEDIATES_BY_BUS_41_59 = new Point[]{
    };
    // 4 - 41
    private static final Point[] INTERMEDIATES_BY_BUS_4_41 = new Point[]{
    };

    private static final Route[] BUS = {
            new Route(4, 16, INTERMEDIATES_BY_BUS_4_16),
            new Route(12, 16, INTERMEDIATES_BY_BUS_12_16),
            new Route(11, 12, null),
            new Route(11, 46, INTERMEDIATES_BY_BUS_11_46),
            new Route(11, 41, INTERMEDIATES_BY_BUS_11_41),
            new Route(41, 46, INTERMEDIATES_BY_BUS_41_46),
            new Route(31, 46, INTERMEDIATES_BY_BUS_31_46),
            new Route(4, 59, null),
            new Route(59, 74, INTERMEDIATES_BY_BUS_59_74),
            new Route(41, 59, INTERMEDIATES_BY_BUS_41_59),
            new Route(4, 41, INTERMEDIATES_BY_BUS_4_41)
    }; // Color.RED
    private static final int BUS_COLOR = Color.RED;

    // 32 - 43
    private static final Point[] INTERMEDIATES_BY_TAXI_DRAGAN_32_43 = new Point[]{
    };
    // 12 - 32
    private static final Point[] INTERMEDIATES_BY_TAXI_DRAGAN_12_32 = new Point[]{
    };
    // 12 - 43
    private static final Point[] INTERMEDIATES_BY_TAXI_DRAGAN_12_43 = new Point[]{
    };

    private static final Route[] TAXI_DRAGAN = {
            new Route(10, 40, null),
            new Route(32, 43, INTERMEDIATES_BY_TAXI_DRAGAN_32_43),
            new Route(12, 32, INTERMEDIATES_BY_TAXI_DRAGAN_12_32),
            new Route(12, 34, INTERMEDIATES_BY_TAXI_DRAGAN_12_43)
    }; // Color.BLUE
    private static final int TAXI_DRAGAN_COLOR = Color.BLUE;

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
