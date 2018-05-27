package service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import service.entity.Curriculum;
import service.repository.BehaviorRepository;
import service.repository.CurriculumRepository;
import service.repository.StudentRepository;
import service.repository.TimeRepository;
import service.service.PositivityService;

@Service
public class PositivityImpl implements PositivityService {
	@Autowired
	private BehaviorRepository behaviorRepo;
	@Autowired
	private TimeRepository timeRepo;
	@Autowired
	private CurriculumRepository curriculumRepo;
	@Autowired
	private StudentRepository studentRepo;

	@Override
	public int[][] getPosivity(int type, int cid) {

		List<String> courseList = curriculumRepo.getCourseName(cid);
		Calendar cal = Calendar.getInstance();
		int length = 1;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		int number = studentRepo.countStudentsByCid(cid);

		if (type == 1) {
			String date = format.format(cal.getTime());
			int day = cal.get(Calendar.DAY_OF_WEEK) - 1;
			return getPosivityOneDay(courseList, date, day, cid, number);
		} else if (type == 2) {
			cal.add(Calendar.DAY_OF_YEAR, -7);
		} else if (type == 3) {
			length = 4;
			cal.add(Calendar.DAY_OF_YEAR, -28);
		} else {
			length = 12;
			cal.add(Calendar.DAY_OF_YEAR, -84);
		}

		String date = format.format(cal.getTime());

		int[][] positivity = new int[courseList.size()][length];

		for (int i = 0; i < courseList.size(); i++) {
			for (int j = 0; j < length; j++) {
				positivity[i][j] = 0;
			}
		}
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < 7; j++) {
				cal.add(Calendar.DAY_OF_YEAR, 1);
				date = format.format(cal.getTime());
				int day = cal.get(Calendar.DAY_OF_WEEK) - 1;
				int[][] oneday = getPosivityOneDay(courseList, date, day, cid, number);
				for (int k = 0; k < courseList.size(); k++) {
					positivity[k][i] += oneday[k][0];
				}
			}
			for (int k = 0; k < courseList.size(); k++) {
				positivity[k][i] /= 7;
			}
		}
		return positivity;
	}

	private int[][] getPosivityOneDay(List<String> courseList, String date, int day, int cid, int number) {

		int[][] positivity = new int[courseList.size()][1];
		for (int i = 0; i < courseList.size(); i++) {
			int count = behaviorRepo.findPositivity(cid, courseList.get(i), date);
			List<Curriculum> c = curriculumRepo.findByCidAndDayAndCourse(cid, day, courseList.get(i));
			if (c != null && c.size() > 0) {
				int hour = timeRepo.getCourseHour(cid, c.get(0).getTid());
				count = (count * 100) / (number * hour) < 100 ? (count * 100) / (number * hour * c.size()) : 100;
				positivity[i][0] = count;

			} else {
				positivity[i][0] = 0;
			}
		}
		return positivity;
	}

	@Override
	public String[] getSubjects(int cid) {
		List<String> courseList = curriculumRepo.getCourseName(cid);
		String[] subjects = new String[courseList.size()];

		for (int i = 0; i < subjects.length; i++) {
			subjects[i] = courseList.get(i);
		}
		return subjects;
	}
}
