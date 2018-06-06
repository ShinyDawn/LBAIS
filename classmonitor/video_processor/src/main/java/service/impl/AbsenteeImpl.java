package service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.entity.Behavior;
import service.model.ActionInfo;
import service.model.Status;
import service.model.InitInfo;
import service.repository.AbsenteeRepository;
import service.repository.BehaviorRepository;

import service.service.AbsenteeService;
import service.tool.DateUtil;
import service.tool.IOHelper;
import service.tool.SeatHelper;

import java.awt.geom.Point2D;
import java.util.*;

/**
 * Created by elva on 2018/5/16.
 */
@Service
public class AbsenteeImpl implements AbsenteeService {
    @Autowired
    private BehaviorRepository behaviorRepository;
    @Autowired
    private AbsenteeRepository absenteeRepository;

//    private Map<Integer, String> mapStatus;
//    private Map<Integer,ActionInfo> mapStatus;
    private int interval = 90; //90帧，1s30帧即每三秒判定一次
    private double rate = 0.3; //精度90帧内有30%检测不到即认为不在

    @Override
    public int analyseAbsentee(List<List<Point2D>> input, InitInfo initInfo) {
        Map<Integer,ActionInfo> map = setInitalStatus(input, initInfo);
        updateStatus(input, initInfo,map);
        return 0;
    }


    private Map<Integer,ActionInfo> setInitalStatus(List<List<Point2D>> input, InitInfo initInfo) {
        Map<Integer, Integer> mapPerSeconds = new HashMap<>();
        Map<Integer, List> seatTable = initInfo.getSeatTable();
        Map<Integer,ActionInfo> mapStatus = new HashMap<>();

        for (Iterator i = seatTable.keySet().iterator(); i.hasNext(); ) {
            Object obj = i.next();
            int sid = (Integer) obj;
            mapPerSeconds.put(sid, 0);
//            int flag = absenteeRepository.countByCidAndSidAndDateAndTid(initInfo.getCid(), sid, initInfo.getDate(), initInfo.getTid());
            int flag = 0;
            if (flag == 1) {
                mapStatus.put(sid, new ActionInfo(Status.approved));
            } else {
                mapStatus.put(sid, new ActionInfo(Status.atSeat));
            }
        }


        for (int j = 0; j < interval; j++) {
            List<Point2D> current = input.get(j);
            Map<Integer, Integer> map = SeatHelper.isAtSeatALL(current);
            for (Iterator i = map.keySet().iterator(); i.hasNext(); ) {
                Object obj = i.next();
                int sid = (Integer) obj;
                int value = mapPerSeconds.get(sid) + map.get(sid);
                mapPerSeconds.put(sid, value);
            }
        }

        for (Iterator i = mapPerSeconds.keySet().iterator(); i.hasNext(); ) {
            Object obj = i.next();
            int sid = (Integer) obj;
            int value = mapPerSeconds.get(sid);
            double currentRate = 1.0 * value / interval;
            if (currentRate < rate && !mapStatus.get(sid).equals(Status.approved)) {
                String start = DateUtil.getDateTime();
//                Behavior behavior = new Behavior();
//                behavior.setAction(Status.absent);
//                behavior.setCid(initInfo.getCid());
//                behavior.setDate(initInfo.getDate());
//                behavior.setPlace(initInfo.getPlace());
//                behavior.setStart(start);
//                behavior.setTid(initInfo.getTid());
//                behaviorRepository.save(behavior);
                mapStatus.put(sid, new ActionInfo(Status.absent,start));
            }
//            System.out.println("key=" + obj + " value=" + mapStatus.get(obj));
        }
        return mapStatus;
    }

    private Map<Integer,ActionInfo> updateStatus(List<List<Point2D>> input, InitInfo initInfo,Map<Integer,ActionInfo> mapStatus) {

        for (int j = 0; j < input.size(); j += interval) {

            Map<Integer, List> seatTable = initInfo.getSeatTable();
            Map<Integer, Integer> mapPer = new HashMap<>();
            for (Iterator i = seatTable.keySet().iterator(); i.hasNext(); ) {
                Object obj = i.next();
                int sid = (Integer) obj;
                mapPer.put(sid, 0);
            }

            for (int k = 0; k < 90; k++) {
                int temp = j + k;
                List<Point2D> current = input.get(temp);

                Map<Integer, Integer> map = SeatHelper.isAtSeatALL(current);

                for (Iterator i = map.keySet().iterator(); i.hasNext(); ) {
                    Object obj = i.next();
                    int sid = (Integer) obj;
                    int value = mapPer.get(sid) + map.get(sid);
                    mapPer.put(sid, value);
                }
            }

            for (Iterator i = mapPer.keySet().iterator(); i.hasNext(); ) {
                Object obj = i.next();
                int sid = (Integer) obj;
                int value = mapPer.get(sid);
                double currentRate = 1.0 * value / (interval);
                if (currentRate < rate) {
                    String status = mapStatus.get(obj).getName().substring(0,2);
                    switch (mapStatus.get(obj).getName()) {
                        //在座
                        case Status.atSeat:
                            String start = DateUtil.getDate();
//                            Behavior behavior = new Behavior();
//                            behavior.setAction(Status.earlyOut);
//                            behavior.setCid(initInfo.getCid());
//                            behavior.setDate(initInfo.getDate());
//                            behavior.setPlace(initInfo.getPlace());
//                            behavior.setStart(start);
//                            behavior.setTid(initInfo.getTid());
//                            behaviorRepository.save(behavior);
                            mapStatus.put(sid, new ActionInfo(Status.earlyOut,start));
                            System.out.println("Time:" + j + "  key=" + obj + "早退");
                            break;
//                        case "zt":
//                            //不作处理
//                            break;
//                        case "qq":
//                            //不作处理
//                            break;
//                        case "qj":
//                            //不作处理
//                            break;
//                            mapStatus.put(sid,"早退");
////                            update behavior
                        default:
                            break;
                    }

                } else {
                    switch (mapStatus.get(obj).getName()) {
//                        case "qj":
//                            break;
                        case Status.absent:
                            String start = mapStatus.get(sid).getStart();
                            String end = DateUtil.getDate();
                            int totalTime = (int)(DateUtil.getTimeLong(end)-DateUtil.getTimeLong(start))/1000;
                            //update behavior 记录迟到 这个行为已经结束
//                            behaviorRepository.updateByTime(totalTime,initInfo.getCid(),sid,initInfo.getDate(),start,end, Status.lateForClass, Status.absent);
                            mapStatus.put(sid, new ActionInfo(Status.atSeat));
                            System.out.println("Time:" + j + "  key=" + obj + Status.lateForClass);
                            break;
                        case Status.earlyOut:
                            start = mapStatus.get(sid).getStart();
                            end = DateUtil.getDate();
                            totalTime = (int)(DateUtil.getTimeLong(end)-DateUtil.getTimeLong(start))/1000;
                            //update behavior 记录迟到 这个行为已经结束
//                            behaviorRepository.updateByTime(totalTime,initInfo.getCid(),sid,initInfo.getDate(),start,end, Status.leave, Status.earlyOut);
                            mapStatus.put(sid,  new ActionInfo(Status.atSeat));
                            //update behavior 离开这个行为已经结束
                            System.out.println("Time:" + j + "  key=" + obj + Status.leave);
                            break;

                        default:
                            break;
                    }
                }
            }
        }
        return mapStatus;
    }

    public static void main(String[] args) throws Exception {
        SeatHelper.init();
        InitInfo initInfo = new InitInfo();
        initInfo.setSeatTable(SeatHelper.getInitalSeatArea());
        initInfo.setCid(2);
        initInfo.setDate("2018-05-17");
        initInfo.setTid(1);
        initInfo.setPlace("语文");
        new AbsenteeImpl().analyseAbsentee(IOHelper.dealWithJson(), initInfo);
    }

}
