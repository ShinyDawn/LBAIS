package service.impl;

import java.util.LinkedList;
import java.util.List;

import service.model.Point;
import service.model.Pose;
import service.model.StudentPose;
import service.service.PoseInitService;
import service.tool.Range;

public class PoseInitImpl implements PoseInitService {

	private double range_y = Range.range_y;

	public Pose[][] init(List<StudentPose> list, Pose[][] pose) {
		LinkedList<Double> heightTable = new LinkedList<Double>();
		LinkedList<LinkedList<StudentPose>> seatTable = new LinkedList<LinkedList<StudentPose>>();

		for (StudentPose p : list) {
			Point position = p.getNeck();
			double height = position.getY();
			if (heightTable.size() == 0) {
				heightTable.add(height);
				seatTable.add(addLine(list, height, pose));
				continue;
			} else if (height > heightTable.get(0) + range_y * 2) {
				heightTable.addFirst(height);
				seatTable.addFirst(addLine(list, height, pose));
				continue;
			}
			for (int i = 0; i < heightTable.size(); i++) {
				double h = heightTable.get(i);
				if (height >= (h - range_y * 2) && height <= (h + range_y * 2)) {
					break;
				} else if (height < (h - range_y * 2)
						&& (i == heightTable.size() - 1 || height > (heightTable.get(i + 1) + range_y * 2))) {
					heightTable.add(i + 1, height);
					seatTable.add(i + 1, addLine(list, height, pose));
					break;
				}
			}
		}

		for (int i = 0; i < seatTable.size() && i < pose.length; i++) {
			LinkedList<StudentPose> line = seatTable.get(i);
			for (int j = 0; j < line.size() && j < pose[i].length; j++) {
				StudentPose sp = line.get(j);
				Point point = sp.getNeck();
				pose[i][j].setX(point.getX());
				pose[i][j].setY(point.getY());
			}
		}
		return pose;
	}

	private LinkedList<StudentPose> addLine(List<StudentPose> list, double height, Pose[][] pose) {
		LinkedList<StudentPose> line = new LinkedList<StudentPose>();

		for (int i = 0; i < list.size(); i++) {
			StudentPose p = list.get(i);
			Point point = p.getNeck();
			double width = point.getX();
			double h = point.getY();
			if (h >= (height - range_y) && h <= (height + range_y)) {
				if (line.size() == 0) {
					line.add(p);
					continue;
				}
				for (int j = 0; j < line.size(); j++) {
					StudentPose sp = line.get(j);
					Point position = sp.getNeck();
					double w = position.getX();
					if (width < w) {
						line.add(j, p);
						break;
					}

					StudentPose last = line.get(line.size() - 1);
					Point lastP = last.getNeck();
					double max_w = lastP.getX();
					if (width > max_w) {
						line.addLast(p);
						break;
					}
				}
			}
		}
		return line;
	}
}
