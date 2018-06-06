package service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import service.repository.BehaviorRepository;
import service.service.VideoHandleService;

@Service
public class VideoHandleImpl implements VideoHandleService {

	@Autowired
	private BehaviorRepository behavior;
	
	public double[] stan_focus=new double[16];
	public double[] focus=new double[16];
	public String[] state=new String[16];
	public int[] time=new int[16];
	//低头抬头的时间超过5个间隔，睡觉的时间超过5个间隔

	@Override
	public void handle(String path) throws Exception {
		// TODO Auto-generated method stub
		File file = new File(path);
		String[] fileName = file.list();
		for (int i = 0; i < fileName.length; i++) {
			String content = readFile(path + "\\" + fileName[i]);
			JSONObject jsonObject = JSONObject.fromObject(content);
			JSONArray people = jsonObject.getJSONArray("people");
			System.out.println("people:" + people.size());
			//
			for (int k = 0; k < 1; k++) {
				// String temp_s = people.getString(k);
				JSONObject person = people.getJSONObject(k);
				// items offered by the json
				String[] items = { "pose_keypoints_2d", "face_keypoints_2d", "hand_left_keypoints_2d",
						"hand_right_keypoints_2d", "pose_keypoints_3d", "face_keypoints_3d", "hand_left_keypoints_3d",
						"hand_right_keypoints_3d" };
				for (int t = 0; t < 1; t++) {
					JSONArray temp = person.getJSONArray(items[i]);
					if(sleep(temp)){
						System.out.println("第"+i/30+"s 睡觉");
						
					}
					if(rise(temp)){
						System.out.println("第"+i/30+"s 抬头");
						
					}
					if(down(temp)){
						System.out.println("第"+i/30+"s 低头");
						
					}
				}
			}
			System.out.println();
		}
	}

	public static String readFile(String path) {
		File file = new File(path);
		BufferedReader reader = null;
		String laststr = "";
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				laststr = laststr + tempString;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return laststr;
	}
	//睡觉  鼻子和眼睛的综合 比 脖子的高度低
	public static boolean sleep(JSONArray pose) throws Exception {
		double nose=getNum(pose, 1);
		double eye_l=getNum(pose,43);
		double eye_r=getNum(pose,46);
		double neck=getNum(pose, 4);
		if(neck==0) return false;
		
		if(nose==0&&eye_l==0&&eye_r==0) return false;
//		if(eye_l==0||eye_r==0)
		double center=(eye_l+eye_r)/4+nose/2;
		
		if(center>neck)return true;
		return false;
	}
	//重心变高
	public static boolean stand() {
		return false;
	}

	public static boolean lie() {
		return false;
	}

	public static boolean sit() {
		return false;
	}

	public static boolean wander() {
		return false;
	}

	// 抬头 右眼高于右耳，即数值较大
	public static boolean rise(JSONArray pose) throws Exception {
		double ear_r=getNum(pose,52);
		double eye_r=getNum(pose,46);
		if(ear_r==0||eye_r==0) return false;
		if (ear_r > eye_r)
			return true;
		return false;
	}

	// 低头 右眼低于右耳，即数值较小
	public static boolean down(JSONArray pose) throws Exception {
		double ear_r=getNum(pose,52);
		double eye_r=getNum(pose,46);
		if(ear_r==0||eye_r==0) return false;
		if (ear_r < eye_r)
			return true;
		return false;
	}
	
	public static double getNum(JSONArray pose,int index) throws Exception{
		if (pose.get(index) instanceof Integer ) return (Integer) pose.get(index) * 1.0;
		else return (Double) pose.get(index);
	}

	public static void main(String[] args) throws Exception {
		String file = "D:\\workspace\\video\\jsonOut\\IMG_1928";
		new VideoHandleImpl().handle(file);
	}
}
