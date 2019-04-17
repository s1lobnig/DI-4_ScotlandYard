package com.example.scotlandyard;

public class Points {
    public static final Point[] POINTS =
            {
                    new Point(46.621253, 14.262028, R.drawable.field1),
                    new Point(46.620646, 14.262115, R.drawable.field2),
                    new Point(46.620211, 14.261976, R.drawable.field3),
                    new Point(46.619851, 14.262282, R.drawable.field4),
                    new Point(46.619267, 14.261555, R.drawable.field5),
                    new Point(46.618915, 14.261932, R.drawable.field6),
                    new Point(46.618504, 14.262092, R.drawable.field7),
                    new Point(46.618630, 14.263643, R.drawable.field8),
                    new Point(46.618869, 14.265377, R.drawable.field9),
                    new Point(46.619086, 14.266723, R.drawable.field10),
                    new Point(46.619140, 14.267265, R.drawable.field11),
                    new Point(46.619716, 14.270423, R.drawable.field12),
                    new Point(46.619881, 14.269706, R.drawable.field13),
                    new Point(46.619716, 14.269127, R.drawable.field14),
                    new Point(46.620191, 14.267992, R.drawable.field15),
                    new Point(46.620515, 14.266065, R.drawable.field16),
                    new Point(46.620097, 14.265880, R.drawable.field17),
                    new Point(46.619281, 14.266640, R.drawable.field18),
                    new Point(46.621057, 14.263084, R.drawable.field19),
                    new Point(46.620747, 14.264639, R.drawable.field20),
                    new Point(46.620468, 14.264055, R.drawable.field21),
                    new Point(46.619809, 14.263529, R.drawable.field22),
                    new Point(46.619899, 14.264574, R.drawable.field23),
                    new Point(46.619829, 14.265035, R.drawable.field24),
                    new Point(46.619237, 14.263813, R.drawable.field25),
                    new Point(46.619327, 14.264427, R.drawable.field26),
                    new Point(46.619342, 14.265392, R.drawable.field27),
                    new Point(46.617959, 14.267656, R.drawable.field28),
                    new Point(46.617368, 14.267608, R.drawable.field29),
                    new Point(46.616377, 14.267945, R.drawable.field30),
                    new Point(46.615188, 14.268267, R.drawable.field31),
                    new Point(46.614255, 14.268112, R.drawable.field32),
                    new Point(46.616794, 14.269750, R.drawable.field33),
                    new Point(46.618219, 14.269460, R.drawable.field34),
                    new Point(46.617973, 14.268688, R.drawable.field35),
                    new Point(46.617150, 14.268741, R.drawable.field36),
                    new Point(46.616623, 14.268987, R.drawable.field37),
                    new Point(46.618221, 14.267194, R.drawable.field38),
                    new Point(46.617842, 14.266911, R.drawable.field39),
                    new Point(46.617737, 14.265319, R.drawable.field40),
                    new Point(46.617642, 14.265404, R.drawable.field41),
                    new Point(46.617441, 14.263678, R.drawable.field42),
                    new Point(46.617170, 14.261776, R.drawable.field43),
                    new Point(46.617439, 14.261448, R.drawable.field44),
                    new Point(46.617306, 14.267350, R.drawable.field45),
                    new Point(46.616782, 14.267175, R.drawable.field46),
                    new Point(46.617016, 14.266424, R.drawable.field47),
                    new Point(46.617086, 14.265898, R.drawable.field48),
                    new Point(46.617030, 14.264802, R.drawable.field49),
                    new Point(46.616983, 14.263730, R.drawable.field50),
                    new Point(46.617061, 14.262915, R.drawable.field51),
                    new Point(46.616934, 14.262426, R.drawable.field52),
                    new Point(46.616417, 14.267055, R.drawable.field53),
                    new Point(46.616537, 14.266264, R.drawable.field54),
                    new Point(46.616271, 14.265796, R.drawable.field55),
                    new Point(46.616203, 14.265063, R.drawable.field56),
                    new Point(46.615855, 14.263554, R.drawable.field57),
                    new Point(46.615830, 14.262251, R.drawable.field58),
                    new Point(46.615701, 14.261529, R.drawable.field59),
                    new Point(46.616158, 14.267683, R.drawable.field60),
                    new Point(46.616086, 14.266365, R.drawable.field61),
                    new Point(46.615721, 14.265209, R.drawable.field62),
                    new Point(46.615399, 14.265027, R.drawable.field63),
                    new Point(46.615537, 14.263772, R.drawable.field64),
                    new Point(46.615337, 14.263295, R.drawable.field65),
                    new Point(46.615174, 14.267126, R.drawable.field66),
                    new Point(46.614759, 14.265927, R.drawable.field67),
                    new Point(46.614963, 14.265145, R.drawable.field68),
                    new Point(46.615059, 14.264641, R.drawable.field69),
                    new Point(46.613225, 14.265681, R.drawable.field70),
                    new Point(46.613952, 14.264406, R.drawable.field71),
                    new Point(46.614596, 14.263340, R.drawable.field72),
                    new Point(46.614718, 14.261804, R.drawable.field73),
                    new Point(46.613512, 14.263024, R.drawable.field74),
                    new Point(46.616090, 14.263997, R.drawable.field75)
            };

    //get coordinat Latitude from spezific Pointnumber
    public static double getLatFromP(int number) {
        return POINTS[number].getLatitude();
    }

    //get coordinat Longitude from spezific Pointnumber
    public static double getLngfromP(int number) {
        return POINTS[number].getLongitude();
    }

    public static Point[] getPoints() {
        return POINTS;
    }

    public static int getIndex(Point p) {
        for (int i = 0; i < POINTS.length; i++) {
            if (POINTS[i].equals(p))
                return i;
        }
        return -1;
    }
}
