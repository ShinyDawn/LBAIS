package process.tool;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import process.model.Point;
import process.model.StudentPose;

public class JsonParser {

	public List<StudentPose> readJson(String str) {
		List<StudentPose> list = new ArrayList<StudentPose>();

		JSONObject object = JSONObject.fromObject(str);
		if (object == null) {
			return list;
		}

		JSONArray people = object.getJSONArray("people");

		if (people != null) {
			for (int i = 0; i < people.size(); i++) {
				JSONObject student = people.getJSONObject(i);
				JSONArray pose = student.getJSONArray("pose_keypoints_2d");
				StudentPose studentPose = new StudentPose();
				if (pose != null) {
					for (int j = 0; j < pose.size(); j += 3) {
						double x = pose.getDouble(j);
						double y = pose.getDouble(j + 1);
						Point point = new Point();
						point.setX(x);
						point.setY(y);
						setPose(j, point, studentPose);
					}
				}
				list.add(studentPose);
			}
		}

		return list;
	}

	private void setPose(int index, Point point, StudentPose pose) {
		switch (index) {
		case 0:
			pose.setNose(point);
			break;
		case 3:
			pose.setNeck(point);
			break;
		case 6:
			pose.setLeftShouder(point);
			break;
		case 9:
			pose.setLeftArm(point);
			break;
		case 12:
			pose.setLeftWrist(point);
			break;
		case 15:
			pose.setRightShouder(point);
			break;
		case 18:
			pose.setRightArm(point);
			break;
		case 21:
			pose.setRightWrist(point);
			break;
		}
	}
}
