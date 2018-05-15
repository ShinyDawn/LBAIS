package service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Observer;

import service.entity.Behavior;
import service.model.Point;
import service.model.Pose;
import service.model.RaiseHand;
import service.model.RaiseHandStudent;
import service.model.StudentPose;
import service.repository.BehaviorRepository;
import service.repository.StudentRepository;
import service.service.AnalyseService;

public class ClassBehaviorAnalyser implements AnalyseService {

	private double range_y = 50;
	private double range_x = 100;
	private static int standup = 0;

	@Override
	public double analyse(List<StudentPose> list, String course, int classroom, Pose[][] pose, Observer observer,
			BehaviorRepository behaviorRepo, StudentRepository studentRepo) {
		boolean found = false;
		double w = 0;
		ArrayList<RaiseHandStudent> rList = new ArrayList<RaiseHandStudent>();

		if (pose == null || pose.length == 0)
			return 0;
		if (pose[0][0].getY() == -1.0) {
			pose[0][0].setY(0);
			pose = init(list, pose);
			return 0;
		}

		for (int i = 0; i < list.size(); i++) {
			StudentPose studentPose = list.get(i);
			Point neck = studentPose.getNeck();

			int raiseLeftHand = analyseRaiseHand(studentPose.getLeftShouder(), studentPose.getLeftArm(),
					studentPose.getLeftWrist());
			int raiseRightHand = analyseRaiseHand(studentPose.getRightShouder(), studentPose.getRightArm(),
					studentPose.getRightWrist());

			int raiseHand = 0;
			if (raiseLeftHand == 1 || raiseRightHand == 1) {
				raiseHand = 1;
			}

			double angle = analyseHeadAngle(studentPose.getNose(), studentPose.getNeck());
			for (int j = 0; j < pose.length; j++) {
				found = false;
				for (int k = 0; k < pose[j].length; k++) {
					double height = pose[j][k].getY();
					double width = pose[j][k].getX();
					if (!pose[j][k].isDected() && neck.getX() >= (width - range_x) && neck.getX() <= (width + range_x)
							&& neck.getY() >= (height - range_y) && neck.getY() <= (height + range_y)) {
						if (raiseHand == 1 && pose[j][k].getRaiseHand() == 0 && pose[j][k].getCid() != 0) {
							pose[j][k].setRaiseHand(raiseHand);
							RaiseHandStudent r = new RaiseHandStudent();
							r.setCid(pose[j][k].getCid());
							r.setSid(pose[j][k].getSid());
							rList.add(r);
						} else if (raiseHand == 0 && pose[j][k].getRaiseHand() == 1 && standup == 0) {

						} else {
							pose[j][k].setRaiseHand(raiseHand);
						}
						pose[j][k].setAngle(angle);
						pose[j][k].setDected(true);
						found = true;
					} else if (pose[j][k].isDected() && neck.getX() >= (width - range_x)
							&& neck.getX() <= (width + range_x) && neck.getY() >= (height - range_y)
							&& neck.getY() <= (height + range_y)) {
						found = false;
					} else {
						found = false;
					}
				}
				if (!found) {
					w = neck.getX();
				}
			}
		}

		return analyseConcentration(course, pose, w, observer, behaviorRepo, studentRepo, rList);
	}

	private double analyseConcentration(String cname, Pose[][] pose, double width, Observer observer,
			BehaviorRepository behaviorRepo, StudentRepository studentRepo, ArrayList<RaiseHandStudent> list) {
		double avg = 0;
		int count = 0;
		standup = 0;

		ArrayList<Double> valueList = new ArrayList<Double>();
		for (int i = 0; i < pose.length; i++) {
			for (int j = 0; j < pose[i].length; j++) {
				Pose p = pose[i][j];
				if (width != 0.0 && p.getCid() != 0 && p.getX() != -1 && !p.isDected() && p.getX() >= (width - range_x)
						&& p.getX() <= (width + range_x)) {
					if (p.getStand() == 0) {
						standup = 1;
						p.setStand(1);
						RaiseHandStudent r = new RaiseHandStudent();
						r.setCid(p.getCid());
						r.setSid(p.getSid());
						list.clear();
						list.add(r);
						for (int k = 0; k < pose.length; k++) {
							for (int m = 0; m < pose[k].length; m++) {
								pose[i][j].setRaiseHand(0);
							}
						}
					}
					width = 0.0;
				} else if (p.getAngle() != 0) {
					p.setStand(0);
					avg += p.getAngle();
					valueList.add(p.getAngle());
					count++;
				}
			}
		}

		if (list.size() > 0) {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			String date = format1.format(cal.getTime());
			if (standup == 0) {
				for (RaiseHandStudent student : list) {
					Behavior b = new Behavior();
					b.setCid(student.getCid());
					b.setSid(student.getSid());
					b.setDate(date);
					b.setAction("举手");
					b.setPlace(cname);
					behaviorRepo.save(b);
					int raise = behaviorRepo.countRaiseHand(student.getCid(), student.getSid(), cname, date);
					int answer = behaviorRepo.countAnswerQuestion(student.getCid(), student.getSid(), cname, date);
					String name = studentRepo.findName(student.getCid(), student.getSid());
					student.setSname(name);
					student.setRaiseHand(raise);
					student.setStandup(answer);
					student.setAnswer(0);
				}
			} else {
				for (RaiseHandStudent student : list) {
					student.setAnswer(1);
					Behavior b = new Behavior();
					b.setCid(student.getCid());
					b.setSid(student.getSid());
					b.setDate(date);
					b.setAction("回答问题");
					b.setPlace(cname);
					behaviorRepo.save(b);
				}
			}
			RaiseHand r = new RaiseHand();
			r.addObserver(observer);
			r.setRaiseHand(list);
			r.setStandup(standup);
			r.notifyObservers(r);
		}

		avg /= count;
		double sdev = 0;
		for (int i = 0; i < valueList.size(); i++) {
			sdev += Math.abs(valueList.get(i) - avg);
		}
		sdev /= count;

		for (int i = 0; i < pose.length; i++) {
			for (int j = 0; j < pose[i].length; j++) {
				Pose p = pose[i][j];
				p.setDected(false);
				double distance = Math.abs(p.getAngle() - avg);
				distance = sdev / distance < 1 ? sdev / distance : 1;
				if (p.getAngle() == 0)
					distance = 1;

				p.setFocus(distance);
			}
		}

		double avgConcentration = 1;
		if (sdev > 1)
			avgConcentration = 1 / Math.log10(sdev);

		return avgConcentration;
	}

	private int analyseRaiseHand(Point shouder, Point arm, Point wrist) {

		double k1 = Math.atan2(shouder.getY() - arm.getY(), shouder.getX() - arm.getX());
		double angle1 = Math.toDegrees(k1);

		double k2 = Math.atan2(wrist.getY() - arm.getY(), wrist.getX() - arm.getX());
		double angle2 = Math.toDegrees(k2);

		if (angle2 >= -30 && angle2 <= 0 && angle1 >= 45 && angle1 <= 90) {
			return 1;
		} else if (angle1 >= -80 && angle1 <= 0 && angle2 >= 110 && angle2 <= 180) {
			return 1;
		}
		return 0;
	}

	private double analyseHeadAngle(Point nose, Point neck) {
		double k = Math.atan2(nose.getY() - neck.getY(), nose.getX() - neck.getX());
		double angle = Math.toDegrees(k);
		return angle;
	}

	private Pose[][] init(List<StudentPose> list, Pose[][] pose) {
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
