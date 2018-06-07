package service.impl;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import service.model.ActionInfo;
import service.model.InitInfo;
import service.model.Status;
import service.tool.DateUtil;
import service.tool.IOHelper;
import service.tool.SeatHelper;

public class TempImpl {
	
	private static int interval = 90; // 90帧，1s30帧即每三秒判定一次
	private static double rate = 0.3; // 精度90帧内有30%检测不到即认为不在
	private static Map<Integer, ActionInfo> map=new HashMap<>();
	
	private static Map<Integer,String> beh=new HashMap<>();
	
	public static void takeClass(String path) throws IOException {
		File file = new File(path);
		String[] fileName = file.list();
		String pathStart=path + "\\" + fileName[0];
		pathStart=pathStart.substring(0,pathStart.length()-19);
		//初始化位置信息
		SeatHelper.init();
		InitInfo initInfo = new InitInfo();
		initInfo.setSeatTable(SeatHelper.getInitalSeatArea());
		initInfo.setCid(2);
		initInfo.setDate("2018-05-17");
		initInfo.setTid(1);
		initInfo.setPlace("语文");
//		new AbsenteeImpl().analyseAbsentee(IOHelper.dealWithJson(), initInfo);
		//初始化状态信息
		beh=new HashMap<>();
		int start=0;
		List<List<Point2D>> temp=getData(start,pathStart);
		map = setInitalStatus(temp, initInfo);
		for (Integer key : map.keySet()) {
		      System.out.println(key+":"+map.get(key).getName());
		    }
		//循环信息
		for(;start<=fileName.length-90;start+=90){
			beh=new HashMap<>();
			start+=90;
			temp=getData(start,pathStart);
			Map<Integer, String> result=updateStatus(start,temp, initInfo, map);
			for (Integer key : result.keySet()) {
			      System.out.println(key+":"+map.get(key).getName());
			    }
		}
		
	}
	
	
	//读90条数据
	public static List<List<Point2D>> getData(int num,String pathStart) throws IOException{
		//String pathStart = "E:\\百度云下载\\2018.5.4\\jsonOut\\IMG_1961\\IMG_1961_00000000";
//      String pathStart = "D:/workspace\\video\\jsonOut/IMG_1961/IMG_1961_00000000";
      DecimalFormat f = new DecimalFormat("0000");
      String pathEnd = "_keypoints.json";

      int start = num;
      int end = num+90;

      List<List<Point2D>> result = new ArrayList<>();
      for (int j = start; j < end; j++) {
          String pathIn = f.format(j);
          String path = pathStart + pathIn + pathEnd;
          File file = new File(path);

          List<Point2D> current = new ArrayList<>();

          String content = FileUtils.readFileToString(file);
          JSONObject object = JSONObject.fromObject(content);
          JSONArray jsonArray = object.getJSONArray("people");

          for (int i = 0; i < jsonArray.size(); i++) {
              JSONObject people = jsonArray.getJSONObject(i);
              JSONArray pose = people.getJSONArray("pose_keypoints_2d");
              Point2D.Double p = new Point2D.Double(pose.getDouble(3), pose.getDouble(4));
              current.add(p);
          }
          result.add(current);
      }
      return result;
	}
	
	public static Map<Integer, ActionInfo> setInitalStatus(List<List<Point2D>> input, InitInfo initInfo) {
		Map<Integer, Integer> mapPerSeconds = new HashMap<>();
		Map<Integer, List> seatTable = initInfo.getSeatTable();
		Map<Integer, ActionInfo> mapStatus = new HashMap<>();

		for (Iterator i = seatTable.keySet().iterator(); i.hasNext();) {
			Object obj = i.next();
			int sid = (Integer) obj;
			mapPerSeconds.put(sid, 0);
			// int flag =
			// absenteeRepository.countByCidAndSidAndDateAndTid(initInfo.getCid(),
			// sid, initInfo.getDate(), initInfo.getTid());
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
			for (Iterator i = map.keySet().iterator(); i.hasNext();) {
				Object obj = i.next();
				int sid = (Integer) obj;
				int value = mapPerSeconds.get(sid) + map.get(sid);
				mapPerSeconds.put(sid, value);
			}
		}

		for (Iterator i = mapPerSeconds.keySet().iterator(); i.hasNext();) {
			Object obj = i.next();
			int sid = (Integer) obj;
			int value = mapPerSeconds.get(sid);
			double currentRate = 1.0 * value / interval;
			if (currentRate < rate && !mapStatus.get(sid).equals(Status.approved)) {
				String start = DateUtil.getDateTime();
				// Behavior behavior = new Behavior();
				// behavior.setAction(Status.absent);
				// behavior.setCid(initInfo.getCid());
				// behavior.setDate(initInfo.getDate());
				// behavior.setPlace(initInfo.getPlace());
				// behavior.setStart(start);
				// behavior.setTid(initInfo.getTid());
				// behaviorRepository.save(behavior);
				mapStatus.put(sid, new ActionInfo(Status.absent, start));
			}
			// System.out.println("key=" + obj + " value=" +
			// mapStatus.get(obj));
		}
		return mapStatus;
	}
	
	public static Map<Integer, String> updateStatus(int j,List<List<Point2D>> input, InitInfo initInfo,
			Map<Integer, ActionInfo> mapStatus) {

//		for (int j = 0; j < input.size(); j += interval) {
//			int j=start;
			Map<Integer, List> seatTable = initInfo.getSeatTable();
			Map<Integer, Integer> mapPer = new HashMap<>();
			//new added
			Map<Integer,String> result=new HashMap<>();
			for (Iterator i = seatTable.keySet().iterator(); i.hasNext();) {
				Object obj = i.next();
				int sid = (Integer) obj;
				mapPer.put(sid, 0);
			}

			for (int k = 0; k < 90; k++) {
//				int temp = j + k;
				List<Point2D> current = input.get(k);

				Map<Integer, Integer> map = SeatHelper.isAtSeatALL(current);

				for (Iterator i = map.keySet().iterator(); i.hasNext();) {
					Object obj = i.next();
					int sid = (Integer) obj;
					int value = mapPer.get(sid) + map.get(sid);
					mapPer.put(sid, value);
				}
			}

			for (Iterator i = mapPer.keySet().iterator(); i.hasNext();) {
				Object obj = i.next();
				int sid = (Integer) obj;
				int value = mapPer.get(sid);
				double currentRate = 1.0 * value / (interval);
				if (currentRate < rate) {
					String status = mapStatus.get(obj).getName().substring(0, 2);
					switch (mapStatus.get(obj).getName()) {
					// 在座
					case Status.atSeat:
						String start = DateUtil.getDate();
						// Behavior behavior = new Behavior();
						// behavior.setAction(Status.earlyOut);
						// behavior.setCid(initInfo.getCid());
						// behavior.setDate(initInfo.getDate());
						// behavior.setPlace(initInfo.getPlace());
						// behavior.setStart(start);
						// behavior.setTid(initInfo.getTid());
						// behaviorRepository.save(behavior);
						mapStatus.put(sid, new ActionInfo(Status.earlyOut, start));
						//System.out.println("Time:" + j + "  key=" + obj + "早退");
						result.put((Integer)obj, "早退");
						break;
					// case "zt":
					// //不作处理
					// break;
					// case "qq":
					// //不作处理
					// break;
					// case "qj":
					// //不作处理
					// break;
					// mapStatus.put(sid,"早退");
					//// update behavior
					default:
						break;
					}

				} else {
					switch (mapStatus.get(obj).getName()) {
					// case "qj":
					// break;
					case Status.absent:
						String start = mapStatus.get(sid).getStart();
						String end = DateUtil.getDate();
						int totalTime = (int) (DateUtil.getTimeLong(end) - DateUtil.getTimeLong(start)) / 1000;
						// update behavior 记录迟到 这个行为已经结束
						// behaviorRepository.updateByTime(totalTime,initInfo.getCid(),sid,initInfo.getDate(),start,end,
						// Status.lateForClass, Status.absent);
						mapStatus.put(sid, new ActionInfo(Status.atSeat));
						//System.out.println("Time:" + j + "  key=" + obj + Status.lateForClass);
						result.put((Integer)obj, "迟到");
						break;
					case Status.earlyOut:
						start = mapStatus.get(sid).getStart();
						end = DateUtil.getDate();
						totalTime = (int) (DateUtil.getTimeLong(end) - DateUtil.getTimeLong(start)) / 1000;
						// update behavior 记录迟到 这个行为已经结束
						// behaviorRepository.updateByTime(totalTime,initInfo.getCid(),sid,initInfo.getDate(),start,end,
						// Status.leave, Status.earlyOut);
						mapStatus.put(sid, new ActionInfo(Status.atSeat));
						// update behavior 离开这个行为已经结束
						//System.out.println("Time:" + j + "  key=" + obj + Status.leave);
						
						result.put((Integer)obj, "离开");
						break;

					default:
						break;
					}
				}
			}
//		}
//		System.out.println();
		map=mapStatus;
		return result;
	}
	
	
	public static Map<Integer,String> getSleep(int start,String path) throws Exception{
		int[] count=new int[12];
		Map<Integer,String> result=new HashMap();
		DecimalFormat f = new DecimalFormat("0000");
	      String pathEnd = "_keypoints.json";
	      for (int j = start; j < start+interval; j++) {
	          String pathIn = f.format(j);
	          String newpath = path + pathIn + pathEnd;
	          File file = new File(newpath);
	          String content = FileUtils.readFileToString(file);
	          JSONObject object = JSONObject.fromObject(content);
	          JSONArray jsonArray = object.getJSONArray("people");

	          for (int i = 0; i < count.length&&i<jsonArray.size(); i++) {
	              JSONObject people = jsonArray.getJSONObject(i);
	              JSONArray pose = people.getJSONArray("pose_keypoints_2d");
	              if(VideoHandleImpl.sleep(pose)) count[i]++;
	          }
	          for(int i=0;i<count.length;i++){
	        	  if(count[i]>=interval*(1-rate))
	        		  result.put(i+1, "睡觉");
	          }
	      }
			
		return result;
	}
	
	
	public static void main(String[] args) throws Exception{
		//TempImpl.takeClass("E:\\百度云下载\\2018.5.4\\jsonOut\\IMG_1961");
		int start=0;
		for(;start<2700;start+=90){
			System.out.println(start);
			Map<Integer,String> temp=TempImpl.getSleep(0, "E:\\百度云下载\\2018.5.4\\jsonOut\\IMG_1960\\IMG_1960_00000000");
			for (Integer key : temp.keySet()) {
			      System.out.println(key+":"+temp.get(key));
			    }
		}
		
	}
}
