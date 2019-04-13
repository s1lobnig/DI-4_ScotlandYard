package com.example.scotlandyard;

public class Points {
    private static final Point[] POINTS =
            {
                    new Point(46.621253, 14.262028),
                    new Point(46.620646, 14.262115),
                    new Point(46.620211, 14.261976),
                    new Point(46.619851, 14.262282),
                    new Point(46.619267, 14.261555),
                    new Point(46.618915, 14.261932),
                    new Point(46.618504, 14.262092),
                    new Point(46.618630, 14.263643),
                    new Point(46.618869, 14.265377),
                    new Point(46.619086, 14.266723),
                    new Point(46.619140, 14.267265),
                    new Point(46.619716, 14.270423),
                    new Point(46.619881, 14.269706),
                    new Point(46.619716, 14.269127),
                    new Point(46.620191, 14.267992),
                    new Point(46.620515, 14.266065),
                    new Point(46.620097, 14.265880),
                    new Point(46.619281, 14.266640),
                    new Point(46.621057, 14.263084),
                    new Point(46.620747, 14.264639),
                    new Point(46.620468, 14.264055),
                    new Point(46.619809, 14.263529),
                    new Point(46.619899, 14.264574),
                    new Point(46.619829, 14.265035),
                    new Point(46.619237, 14.263813),
                    new Point(46.619327, 14.264427),
                    new Point(46.619342, 14.265392),
                    new Point(46.617959, 14.267656),
                    new Point(46.617368, 14.267608),
                    new Point(46.616377, 14.267945),
                    new Point(46.615188, 14.268267),
                    new Point(46.614255, 14.268112),
                    new Point(46.616794, 14.269750),
                    new Point(46.618219, 14.269460),
                    new Point(46.617973, 14.268688),
                    new Point(46.617150, 14.268741),
                    new Point(46.616623, 14.268987),
                    new Point(46.618221, 14.267194),
                    new Point(46.617842, 14.266911),
                    new Point(46.617737, 14.265319),
                    new Point(46.617642, 14.265404),
                    new Point(46.617441, 14.263678),
                    new Point(46.617170, 14.261776),
                    new Point(46.617439, 14.261448),
                    new Point(46.617306, 14.267350),
                    new Point(46.616782, 14.267175),
                    new Point(46.617016, 14.266424),
                    new Point(46.617086, 14.265898),
                    new Point(46.617030, 14.264802),
                    new Point(46.616983, 14.263730),
                    new Point(46.617061, 14.262915),
                    new Point(46.616934, 14.262426),
                    new Point(46.616417, 14.267055),
                    new Point(46.616537, 14.266264),
                    new Point(46.616271, 14.265796),
                    new Point(46.616203, 14.265063),
                    new Point(46.615855, 14.263554),
                    new Point(46.615830, 14.262251),
                    new Point(46.615701, 14.261529),
                    new Point(46.616158, 14.267683),
                    new Point(46.616086, 14.266365),
                    new Point(46.615721, 14.265209),
                    new Point(46.615399, 14.265027),
                    new Point(46.615537, 14.263772),
                    new Point(46.615337, 14.263295),
                    new Point(46.615174, 14.267126),
                    new Point(46.614759, 14.265927),
                    new Point(46.614963, 14.265145),
                    new Point(46.615059, 14.264641),
                    new Point(46.613225, 14.265681),
                    new Point(46.613952, 14.264406),
                    new Point(46.614596, 14.263340),
                    new Point(46.614718, 14.261804),
                    new Point(46.613512, 14.263024),
                    new Point(46.616203, 14.265063) //it is point 56!
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
