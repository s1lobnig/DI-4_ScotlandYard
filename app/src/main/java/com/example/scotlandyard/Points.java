package com.example.scotlandyard;

public class Points {
    private Point[] dots = new Point[75];

    public Points() {
        dots[0] = new Point(46.616203, 14.265063); //it is point 56!
        dots[1] = new Point(46.621253, 14.262028);
        dots[2] = new Point(46.620646, 14.262115);
        dots[3] = new Point(46.620211, 14.261976);
        dots[4] = new Point(46.619851, 14.262282);
        dots[5] = new Point(46.619267, 14.261555);
        dots[6] = new Point(46.618915, 14.261932);
        dots[7] = new Point(46.618504, 14.262092);
        dots[8] = new Point(46.618630, 14.263643);
        dots[9] = new Point(46.618869, 14.265377);
        dots[10] = new Point(46.619086, 14.266723);
        dots[11] = new Point(46.619140, 14.267265);
        dots[12] = new Point(46.619716, 14.270423);
        dots[13] = new Point(46.619881, 14.269706);
        dots[14] = new Point(46.619716, 14.269127);
        dots[15] = new Point(46.620191, 14.267992);
        dots[16] = new Point(46.620515, 14.266065);
        dots[17] = new Point(46.620097, 14.265880);
        dots[18] = new Point(46.619281, 14.266640);
        dots[19] = new Point(46.621057, 14.263084);
        dots[20] = new Point(46.620747, 14.264639);
        dots[21] = new Point(46.620468, 14.264055);
        dots[22] = new Point(46.619809, 14.263529);
        dots[23] = new Point(46.619899, 14.264574);
        dots[24] = new Point(46.619829, 14.265035);
        dots[25] = new Point(46.619237, 14.263813);
        dots[26] = new Point(46.619327, 14.264427);
        dots[27] = new Point(46.619342, 14.265392);
        dots[28] = new Point(46.617959, 14.267656);
        dots[29] = new Point(46.617368, 14.267608);
        dots[30] = new Point(46.616377, 14.267945);
        dots[31] = new Point(46.615188, 14.268267);
        dots[32] = new Point(46.614255, 14.268112);
        dots[33] = new Point(46.616794, 14.269750);
        dots[34] = new Point(46.618219, 14.269460);
        dots[35] = new Point(46.617973, 14.268688);
        dots[36] = new Point(46.617150, 14.268741);
        dots[37] = new Point(46.616623, 14.268987);
        dots[38] = new Point(46.618221, 14.267194);
        dots[39] = new Point(46.617842, 14.266911);
        dots[40] = new Point(46.617737, 14.265319);
        dots[41] = new Point(46.617642, 14.265404);
        dots[42] = new Point(46.617441, 14.263678);
        dots[43] = new Point(46.617170, 14.261776);
        dots[44] = new Point(46.617439, 14.261448);
        dots[45] = new Point(46.617306, 14.267350);
        dots[46] = new Point(46.616782, 14.267175);
        dots[47] = new Point(46.617016, 14.266424);
        dots[48] = new Point(46.617086, 14.265898);
        dots[49] = new Point(46.617030, 14.264802);
        dots[50] = new Point(46.616983, 14.263730);
        dots[51] = new Point(46.617061, 14.262915);
        dots[52] = new Point(46.616934, 14.262426);
        dots[53] = new Point(46.616417, 14.267055);
        dots[54] = new Point(46.616537, 14.266264);
        dots[55] = new Point(46.616271, 14.265796);
        dots[56] = new Point(46.616203, 14.265063);
        dots[57] = new Point(46.615855, 14.263554);
        dots[58] = new Point(46.615830, 14.262251);
        dots[59] = new Point(46.615701, 14.261529);
        dots[60] = new Point(46.616158, 14.267683);
        dots[61] = new Point(46.616086, 14.266365);
        dots[62] = new Point(46.615721, 14.265209);
        dots[63] = new Point(46.615399, 14.265027);
        dots[64] = new Point(46.615537, 14.263772);
        dots[65] = new Point(46.615337, 14.263295);
        dots[66] = new Point(46.615174, 14.267126);
        dots[67] = new Point(46.614759, 14.265927);
        dots[68] = new Point(46.614963, 14.265145);
        dots[69] = new Point(46.615059, 14.264641);
        dots[70] = new Point(46.613225, 14.265681);
        dots[71] = new Point(46.613952, 14.264406);
        dots[72] = new Point(46.614596, 14.263340);
        dots[73] = new Point(46.614718, 14.261804);
        dots[74] = new Point(46.613512, 14.263024);
    }

    //get coordinat X from spezific Pointnumber
    public double getXfromP(int number){
        return dots[number].getX();
    }

    //get coordinat Y from spezific Pointnumber
    public double getYfromP(int number){
        return dots[number].getY();
    }

    public Point[] getDots() {
        return dots;
    }
}
