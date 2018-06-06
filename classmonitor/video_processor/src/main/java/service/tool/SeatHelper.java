package service.tool;

import javax.persistence.criteria.CriteriaBuilder;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.*;

/**
 * Created by elva on 2018/5/16.
 */
public class SeatHelper {

    //初始的座位表，学号 + 座位区域
    private static Map<Integer, List> initalSeatArea=new HashMap<>();

    public static final double offset_x = 40.0;
    public static final double offset_y=40.0;

    public static void init(){
        Point2D.Double p0 = new Point2D.Double(739.189,374.348);
        Point2D.Double p1 = new Point2D.Double(1492.6,388.972);
        Point2D.Double p2 = new Point2D.Double(1657.44,556.836);
        Point2D.Double p3 = new Point2D.Double(1845.88,483.3);
        Point2D.Double p4 = new Point2D.Double(1195.51,318.474);
        Point2D.Double p5 = new Point2D.Double(954.065,709.86);
        Point2D.Double p6 = new Point2D.Double(1348.43,597.874);
        Point2D.Double p7 = new Point2D.Double(612.726,527.313);
        Point2D.Double p8 = new Point2D.Double(1548.69,294.738);
        Point2D.Double p9 = new Point2D.Double(971.683,483.347);
        Point2D.Double p10 = new Point2D.Double(1377.98,312.452);
        Point2D.Double p11 = new Point2D.Double(927.521,235.902);
        Point2D.Double p12 = new Point2D.Double(386.061,412.618);
        Point2D.Double[] points = {p0,p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12};
        for (int i = 0; i < points.length; i++) {
            //这里i为学号
            initalSeatArea.put(i+1, SeatHelper.getSeatShape(points[i]));
        }
    }

    public static Map<Integer, List> getInitalSeatArea() {
        return initalSeatArea;
    }

    public static List getSeatShape(Point2D.Double point) {
        List list = new ArrayList();
        Point2D.Double p1 = new Point2D.Double(point.getX() - offset_x, point.getY() - offset_y);
        Point2D.Double p3 = new Point2D.Double(point.getX() - offset_x, point.getY() + offset_y);
        Point2D.Double p5 = new Point2D.Double(point.getX() + offset_x, point.getY() + offset_y);
        Point2D.Double p7 = new Point2D.Double(point.getX() + offset_x, point.getY() - offset_y);
        Point2D.Double[] points = {p1, p3, p5, p7};
        for (int i = 0; i < points.length; i++) {
            list.add(points[i]);
        }
        return list;
    }

    public static boolean isInArea(Point2D point, List<Point2D.Double> area) {
        GeneralPath generalPath = new GeneralPath();
        Point2D.Double start = area.get(0);
        generalPath.moveTo(start.x, start.y);
        for (Point2D.Double p : area) {
            generalPath.lineTo(p.x, p.y);
        }
        generalPath.lineTo(start.x, start.y);
        generalPath.closePath();
        return generalPath.contains(point);
    }

    public static boolean isAtSeat(int sid, List<Point2D> current) {
        for (Point2D p : current) {
            if (isInArea(p, initalSeatArea.get(sid))) {
                return true;
            }
        }
        return false;
    }

    public static Map<Integer, Integer> isAtSeatALL(List<Point2D> current) {
        Map<Integer, Integer> map = new HashMap<>();
        for (Iterator i = initalSeatArea.keySet().iterator(); i.hasNext(); ) {
            Object obj = i.next();
            int sid = (Integer) obj;
            int isAtSeat = isAtSeat(sid, current) ? 1 : 0;
            map.put(sid, isAtSeat);
        }
        return map;
    }
}
