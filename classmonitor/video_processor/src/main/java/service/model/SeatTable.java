package service.model;

import service.tool.SeatHelper;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by elva on 2018/5/21.
 */
public class SeatTable {

    public static final Map<Integer,Point2D.Double> neckTable = new HashMap<Integer,Point2D.Double>() {
        {
//            Point2D.Double p0 = new Point2D.Double(739.189, 374.348);
//            Point2D.Double p1 = new Point2D.Double(1492.6, 388.972);
//            Point2D.Double p2 = new Point2D.Double(1657.44, 556.836);
//            Point2D.Double p3 = new Point2D.Double(1845.88, 483.3);
//            Point2D.Double p4 = new Point2D.Double(1195.51, 318.474);
//            Point2D.Double p5 = new Point2D.Double(954.065, 709.86);
//            Point2D.Double p6 = new Point2D.Double(1348.43, 597.874);
//            Point2D.Double p7 = new Point2D.Double(612.726, 527.313);
//            Point2D.Double p8 = new Point2D.Double(1548.69, 294.738);
//            Point2D.Double p9 = new Point2D.Double(971.683, 483.347);
//            Point2D.Double p10 = new Point2D.Double(1377.98, 312.452);
//            Point2D.Double p11 = new Point2D.Double(927.521, 235.902);
//            Point2D.Double p12 = new Point2D.Double(386.061, 412.618);
            Point2D.Double p0= new Point2D.Double(1636.85,527.302);
            Point2D.Double p1= new Point2D.Double(1589.98,262.438);
            Point2D.Double p2= new Point2D.Double(592.031,468.431);
            Point2D.Double p3= new Point2D.Double(1486.78,344.911);
            Point2D.Double p4= new Point2D.Double(986.485,294.964);
            Point2D.Double p5= new Point2D.Double(1377.84,253.668);
            Point2D.Double p6= new Point2D.Double(1330.87,556.756);
            Point2D.Double p7= new Point2D.Double(951.26,433.02);
            Point2D.Double p8= new Point2D.Double(1831.19,462.622);
            Point2D.Double p9= new Point2D.Double(721.618,309.493);
            Point2D.Double p10= new Point2D.Double(930.484,665.644);
            Point2D.Double p11= new Point2D.Double(1189.73,291.782);
            Point2D.Double p12= new Point2D.Double(403.707,350.723);
            Point2D.Double[] points = {p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12};
            for (int i = 0; i < points.length; i++) {
                //这里i为学号
                put(i + 1, points[i]);
            }
        }
    };

}
