package com.example.scotlandyard;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author René
 * <p>
 * this class contains the routes between the @POINTS + additional methods
 */
public class Routes {

    private Routes() {

    }

    public static final float ROUTE_WIDTH = 15f;
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_5_6 = {
            new Point(46.618745, 14.261462),
            new Point(46.618712, 14.261580),
    };
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_20_21 = {
            new Point(46.620758, 14.264536),
            new Point(46.620544, 14.264391)
    };
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_21_22 = {
            new Point(46.620322, 14.264098),
            new Point(46.620281, 14.263773),
            new Point(46.620164, 14.263646)
    };
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_28_29 = {
            new Point(46.617871, 14.267544),
            new Point(46.617710, 14.267488)
    };
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_21_23 = {
            new Point(46.620322, 14.264098),
            new Point(46.620149, 14.264186),
            new Point(46.620059, 14.264390),
            new Point(46.619938, 14.264483)
    };
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_21_24 = {
            new Point(46.620322, 14.264098),
            new Point(46.620252, 14.264625),
            new Point(46.619966, 14.265080)
    };
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_22_23 = {
            new Point(46.619799, 14.263644),
            new Point(46.619712, 14.263757),
            new Point(46.619694, 14.264272),
            new Point(46.619821, 14.264491)
    };
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_23_24 = {
            new Point(46.620018, 14.264658),
            new Point(46.619990, 14.264842),
            new Point(46.619902, 14.264867)
    };
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_24_27 = {
            new Point(46.619743, 14.265144),
            new Point(46.619589, 14.265078),
            new Point(46.619292, 14.265090),
            new Point(46.619268, 14.265285)
    };
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_24_26 = {
            new Point(46.619624, 14.264825),
            new Point(46.619516, 14.264847),
            new Point(46.619496, 14.264725),
            new Point(46.619354, 14.264780),
            new Point(46.619262, 14.264750)
    };
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_26_27 = {
            new Point(46.619262, 14.264750),
            new Point(46.619233, 14.264938),
            new Point(46.619292, 14.265090),
            new Point(46.619268, 14.265285)
    };
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_22_25 = {
            new Point(46.619693, 14.263541),
            new Point(46.619650, 14.263599),
            new Point(46.619327, 14.263563),
            new Point(46.619357, 14.263670)
    };
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_25_26 = {
            new Point(46.619375, 14.264148)
    };
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_10_38 = {
            new Point(46.619035, 14.266970)
    };
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_28_35 = {
            new Point(46.617947, 14.268011),
            new Point(46.617894, 14.268020),
            new Point(46.617891, 14.268264),
            new Point(46.617975, 14.268311)
    };
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_29_35 = {
            new Point(46.617451, 14.267580),
            new Point(46.617546, 14.267736),
            new Point(46.617672, 14.267704),
            new Point(46.617681, 14.268023),
            new Point(46.617975, 14.268311)
    };
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_29_36 = {
            new Point(46.617385, 14.267708),
            new Point(46.617206, 14.267824),
            new Point(46.617210, 14.268709)
    };
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_36_37 = {
            new Point(46.617110, 14.268567),
            new Point(46.616641, 14.268528)
    };
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_42_51 = {
            new Point(46.617284, 14.263694),
            new Point(46.617202, 14.262918)
    };
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_45_46 = {
            new Point(46.617133, 14.267060)
    };
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_46_47 = {
            new Point(46.616966, 14.267022),
            new Point(46.617067, 14.266893)
    };
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_46_53 = {
            new Point(46.616565, 14.267152)
    };
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_47_48 = {
            new Point(46.617016, 14.266255)
    };
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_47_54 = {
            new Point(46.616724, 14.266393)
    };
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_50_51 = {
            new Point(46.616897, 14.263025),
            new Point(46.617061, 14.263001)
    };
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_53_61 = {
            new Point(46.616351, 14.266395)
    };
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_56_62 = {
            new Point(46.616211, 14.265390),
            new Point(46.615729, 14.265454)
    };
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_59_73 = {
            new Point(46.615511, 14.261994)
    };
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_62_63 = {
            new Point(46.615581, 14.265248)
    };
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_64_65 = {
            new Point(46.615629, 14.263624),
            new Point(46.615572, 14.263472),
            new Point(46.615478, 14.263423)
    };
    private static final Point[] INTERMEDIATES_BY_FOOT_PTS_71_74 = {
            new Point(46.613368, 14.264279)
    };

    private static final Route[] BY_FOOT = {
            new Route(1, 2),
            new Route(2, 3),
            new Route(2, 4),
            new Route(3, 5),
            new Route(4, 6),
            new Route(5, 6, INTERMEDIATES_BY_FOOT_PTS_5_6),
            new Route(6, 7),
            new Route(7, 8),
            new Route(8, 9),
            new Route(9, 10),
            new Route(10, 11),
            new Route(10, 18),
            new Route(10, 38, INTERMEDIATES_BY_FOOT_PTS_10_38),
            new Route(12, 13),
            new Route(13, 14),
            new Route(14, 15),
            new Route(16, 17),
            new Route(17, 24),
            new Route(19, 20),
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
            new Route(28, 38),
            new Route(28, 35, INTERMEDIATES_BY_FOOT_PTS_28_35),
            new Route(28, 29, INTERMEDIATES_BY_FOOT_PTS_28_29),
            new Route(28, 39),
            new Route(29, 36, INTERMEDIATES_BY_FOOT_PTS_29_36),
            new Route(29, 35, INTERMEDIATES_BY_FOOT_PTS_29_35),
            new Route(29, 45),
            new Route(30, 37),
            new Route(30, 53),
            new Route(30, 60),
            new Route(31, 66),
            new Route(32, 66),
            new Route(33, 36),
            new Route(33, 37),
            new Route(34, 35),
            new Route(36, 37, INTERMEDIATES_BY_FOOT_PTS_36_37),
            new Route(38, 39),
            new Route(40, 41),
            new Route(41, 48),
            new Route(42, 49),
            new Route(42, 50),
            new Route(42, 51, INTERMEDIATES_BY_FOOT_PTS_42_51),
            new Route(43, 52),
            new Route(43, 44),
            new Route(43, 51),
            new Route(45, 46, INTERMEDIATES_BY_FOOT_PTS_45_46),
            new Route(46, 47, INTERMEDIATES_BY_FOOT_PTS_46_47),
            new Route(46, 53, INTERMEDIATES_BY_FOOT_PTS_46_53),
            new Route(47, 48, INTERMEDIATES_BY_FOOT_PTS_47_48),
            new Route(47, 54, INTERMEDIATES_BY_FOOT_PTS_47_54),
            new Route(48, 49),
            new Route(50, 51, INTERMEDIATES_BY_FOOT_PTS_50_51),
            new Route(51, 52),
            new Route(52, 58),
            new Route(53, 61, INTERMEDIATES_BY_FOOT_PTS_53_61),
            new Route(54, 55),
            new Route(55, 61),
            new Route(55, 56),
            new Route(56, 62, INTERMEDIATES_BY_FOOT_PTS_56_62),
            new Route(56, 63),
            new Route(56, 75),
            new Route(57, 64),
            new Route(57, 75),
            new Route(57, 58),
            new Route(58, 59),
            new Route(59, 73, INTERMEDIATES_BY_FOOT_PTS_59_73),
            new Route(60, 61),
            new Route(60, 66),
            new Route(66, 67),
            new Route(62, 63, INTERMEDIATES_BY_FOOT_PTS_62_63),
            new Route(63, 64),
            new Route(63, 68),
            new Route(64, 65, INTERMEDIATES_BY_FOOT_PTS_64_65),
            new Route(65, 69),
            new Route(65, 72),
            new Route(68, 67),
            new Route(68, 69),
            new Route(69, 71),
            new Route(70, 71),
            new Route(71, 72),
            new Route(71, 74, INTERMEDIATES_BY_FOOT_PTS_71_74),
            new Route(72, 74),
            new Route(72, 73)
    };
    private static final int BY_FOOT_COLOR = Color.YELLOW;

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
    private static final int BICYCLE_COLOR = Color.rgb(255, 164, 17);

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

    private static final Route[] BUS = {
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
    private static final int BUS_COLOR = Color.RED;

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

    /**
     * get a route by BICYCLE between current and next point
     *
     * @param current ...current point (point number)
     * @param next    ......next point (point number)
     * @return route - if found
     * null - else
     */
    private static Route getByBicycleRoute(int current, int next) {
        for (Route r : BICYCLE) {
            if ((r.getStartPoint() == current && r.getEndPoint() == next) || (r.getStartPoint() == next && r.getEndPoint() == current)) {
                return r;
            }
        }
        return null;
    }

    /**
     * get a route by FOOT between current and next point
     *
     * @param current ...current point (point number)
     * @param next    ......next point (point number)
     * @return route - if found
     * null - else
     */
    private static Route getByFootRoute(int current, int next) {
        for (Route r : BY_FOOT) {
            if ((r.getStartPoint() == current && r.getEndPoint() == next) || (r.getStartPoint() == next && r.getEndPoint() == current)) {
                return r;
            }
        }
        return null;
    }

    /**
     * get a route by BUS between current and next point
     *
     * @param current ...current point (point number)
     * @param next    ......next point (point number)
     * @return route - if found
     * null - else
     */
    private static Route getByBusRoute(int current, int next) {
        for (Route r : BUS) {
            if ((r.getStartPoint() == current && r.getEndPoint() == next) || (r.getStartPoint() == next && r.getEndPoint() == current)) {
                return r;
            }
        }
        return null;
    }

    /**
     * get a route by TAXI_DRAGAN between current and next point
     *
     * @param current ...current point (point number)
     * @param next    ......next point (point number)
     * @return route - if found
     * null - else
     */
    private static Route getByTaxiDraganRoute(int current, int next) {
        for (Route r : TAXI_DRAGAN) {
            if ((r.getStartPoint() == current && r.getEndPoint() == next) || (r.getStartPoint() == next && r.getEndPoint() == current)) {
                return r;
            }
        }
        return null;
    }

    /**
     * Returns a 3-Parameter-Object-[] corresponding to the inputs...
     *
     * @param current ...current Player-Location
     * @param next    ......new Player-Location
     * @return Object[]:
     * [0]: boolean isValid.....true - Valid new Location for current Location
     * false - else
     * [1]: Route route.........resulting route - if valid
     * null - else
     * [2]: int vehicleCode.....Vehicle code:
     * 0 = foot
     * 1 = bicycle
     * 2 = bus
     * 3 = taxi dragan
     * -1 = invalid
     */
    public static Object[] getRoute(int current, int next) {
        Route foot = getByFootRoute(current + 1, next + 1);
        if (foot != null)
            return new Object[]{true, foot, 0};
        Route bicycle = getByBicycleRoute(current + 1, next + 1);
        if (bicycle != null)
            return new Object[]{true, bicycle, 1};
        Route bus = getByBusRoute(current + 1, next + 1);
        if (bus != null)
            return new Object[]{true, bus, 2};
        Route taxiDragan = getByTaxiDraganRoute(current + 1, next + 1);
        if (taxiDragan != null)
            return new Object[]{true, taxiDragan, 3};
        return new Object[]{false, null, -1};
    }

    private static ArrayList<Route> getAllByFoot(int point, int notToInclude) {
        ArrayList<Route> routes = new ArrayList<>();
        for (Route r : BY_FOOT) {
            if ((r.getStartPoint() == point && r.getEndPoint() != notToInclude) || (r.getEndPoint() == point && r.getStartPoint() != notToInclude)) {
                routes.add(r);
            }
        }
        if (routes.size() == 0) {
            return null;
        }
        return routes;
    }

    private static ArrayList<Route> getAllByBicycle(int point, int notToInclude) {
        ArrayList<Route> routes = new ArrayList<>();
        for (Route r : BICYCLE) {
            if ((r.getStartPoint() == point && r.getEndPoint() != notToInclude) || (r.getEndPoint() == point && r.getStartPoint() != notToInclude)) {
                routes.add(r);
            }
        }
        if (routes.size() == 0) {
            return null;
        }
        return routes;
    }

    private static ArrayList<Route> getAllByBus(int point, int notToInclude) {
        ArrayList<Route> routes = new ArrayList<>();
        for (Route r : BUS) {
            if ((r.getStartPoint() == point && r.getEndPoint() != notToInclude) || (r.getEndPoint() == point && r.getStartPoint() != notToInclude)) {
                routes.add(r);
            }
        }
        if (routes.size() == 0) {
            return null;
        }
        return routes;
    }

    private static ArrayList<Route> getAllByTaxiDragan(int point, int notToInclude) {
        ArrayList<Route> routes = new ArrayList<>();
        for (Route r : TAXI_DRAGAN) {
            if ((r.getStartPoint() == point && r.getEndPoint() != notToInclude) || (r.getEndPoint() == point && r.getStartPoint() != notToInclude)) {
                routes.add(r);
            }
        }
        if (routes.size() == 0) {
            return null;
        }
        return routes;
    }


    public static Object[] getRandomRoute(int current, int notNext) {
        ArrayList<Route> foot = getAllByFoot(current + 1, notNext + 1);
        ArrayList<Route> bicycle = getAllByBicycle(current + 1, notNext + 1);
        ArrayList<Route> bus = getAllByBus(current + 1, notNext + 1);
        ArrayList<Route> taxiDragan = getAllByTaxiDragan(current + 1, notNext + 1);
        if (foot != null) {
            return new Object[]{true, foot.get((new Random()).nextInt(foot.size())), 0};
        } else if (bicycle != null) {
            return new Object[]{true, bicycle.get((new Random()).nextInt(bicycle.size())), 1};
        } else if (bus != null) {
            return new Object[]{true, bus.get((new Random()).nextInt(bus.size())), 2};
        } else if (taxiDragan != null) {
            return new Object[]{true, taxiDragan.get((new Random()).nextInt(taxiDragan.size())), 3};
        } else {
            return new Object[]{false, null, -1};
        }
    }
}
