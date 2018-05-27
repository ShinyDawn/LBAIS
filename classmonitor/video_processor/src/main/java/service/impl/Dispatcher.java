package service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Observer;

import service.entity.Behavior;
import service.entity.Student;
import service.entity.Time;
import service.model.Pose;
import service.model.StudentPose;
import service.repository.BehaviorRepository;
import service.repository.CurriculumRepository;
import service.repository.StudentRepository;
import service.repository.TimeRepository;
import service.service.AnalyseService;
import service.service.DispatchService;
import service.service.PoseService;
import service.service.SourceService;

public class Dispatcher implements DispatchService {
	private static CurriculumRepository curriculumRepo;
	private static BehaviorRepository behaviorRepo;
	private static StudentRepository studentRepo;
	private static List<Time> list;
	private static int classroom;
	public static Pose[][] pose;
	private static boolean end = false;
	private static Time timestamp = null;
	private static String cname = null;
	public static Observer observer;

	@Override
	public void init(int cid, TimeRepository timeRepo, CurriculumRepository curriculum, BehaviorRepository behavior,
			StudentRepository student, Observer observe) {
		curriculumRepo = curriculum;
		behaviorRepo = behavior;
		studentRepo = student;
		classroom = cid;
		list = timeRepo.findByCid(classroom);
		observer = observe;

		List<Student> slist = studentRepo.findByCid(cid);
		int max_x = 0;
		int max_y = 0;

		for (Student s : slist) {
			if (s.getX() > max_x)
				max_x = s.getX();
			if (s.getY() > max_y)
				max_y = s.getY();
		}

		pose = new Pose[max_x][max_y];

		for (int i = 0; i < max_x; i++) {
			for (int j = 0; j < max_y; j++) {
				Pose p = new Pose();
				p.setCid(cid);
				pose[i][j] = p;
			}
		}

		for (int i = 0; i < slist.size(); i++) {
			Student s = slist.get(i);
			Pose p = pose[s.getX() - 1][s.getY() - 1];
			p.setSid(s.getSid());
		}
	}

	@Override
	public void dispatch(SourceService source, PoseService poseService, AnalyseService classBehavior) {
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_WEEK) - 1;
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		String time = format.format(cal.getTime());

		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-mm-dd");
		String date = format1.format(cal.getTime());

		boolean isClass = false;

		if (timestamp != null) {
			String[] t = timestamp.getTime().split("-");
			if (time.compareTo(t[0]) >= 0 && time.compareTo(t[0]) <= 0) {
				cname = curriculumRepo.findByTidAndCidAndDay(timestamp.getId(), timestamp.getCid(),day).getCourse();
				isClass = true;
				end = true;
			}
		}
		if (!isClass) {
			for (Time t : list) {
				String[] times = t.getTime().split("-");
				if (time.compareTo(times[0]) >= 0 && time.compareTo(times[1]) <= 0) {
					timestamp = t;
					cname = curriculumRepo.findByTidAndCidAndDay(t.getId(),t.getCid(), day).getCourse();
					isClass = true;
					end = true;
					break;
				}
			}
		}

		if (isClass) {
			String filename = source.getNextSource();
			List<StudentPose> list = poseService.getStudentPose(filename);
			classBehavior.analyse(list, cname, classroom, pose, observer,behaviorRepo, studentRepo);
		} else {
			if (end) {
				for (int i = 0; i < pose.length; i++) {
					for (int j = 0; j < pose[0].length; j++) {
						Pose p = pose[i][j];
						Behavior b = new Behavior();
						b.setCid(p.getCid());
						b.setSid(p.getSid());
						b.setTotal_time((int) (p.getFocus() * 100));
						b.setDate(date);
						b.setAction("concentration");
						b.setPlace(cname);
						behaviorRepo.save(b);
					}
				}
				end = false;
			}
		}
	}

	@Override
	public Pose[][] getPose() {
		return pose;
	}
}
