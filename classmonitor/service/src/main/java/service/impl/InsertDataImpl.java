package service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import service.service.InsertDataService;
import service.entity.Behavior;
import service.entity.Curriculum;
import service.entity.Student;
import service.entity.Time;
import service.repository.*;
import service.util.DateUtil;

/**
 * 注意，Curriculum表的date字段没有赋值
 *
 * @author 云奎
 */
@Service
public class InsertDataImpl implements InsertDataService {

	//@Autowired
	//private CurriculumRepository CurriculumRepository;
	@Autowired
	private StudentRepository StudentRepository;
	//@Autowired
	//private TimeRepository TimeRepository;
	@Autowired
	private BehaviorRepository BehaviorRepository;

	// 班级编号
	private static final int cid = 2;
	// 插值周数
	private static final int week = 16;

	private static final int max_frequency = 10;

	@Override
	public void insert() {
		// 全删
		//CurriculumRepository.deleteAll();
		StudentRepository.deleteAll();
		//TimeRepository.deleteAll();
		BehaviorRepository.deleteAll();

		// 学生
		String[] student = { "钱多多", "张晓伟", "王小明", "李小娜", "王小峰", "韩小雪", "周小芳", "赵小磊", "陆小瑶", "刘小洋", "叶小天", "王小明", "赵小川",
				"李小静", "吴小杰", "孙小豪", "郑小波" };
		String[] gender = { "男", "男", "男", "女", "男", "女", "女", "男", "女", "女", "男", "男", "男", "女", "男", "男", "男" };

		for (int i = 0; i < student.length; i++) {
			Student s = new Student();
			s.setSid(i + 1);
			s.setName(student[i]);
			s.setGender(gender[i]);
			s.setCid(cid);
			s.setX(i / 6 + 1);
			s.setY(i % 6 + 1);
			if (i >= 14) {
				s.setX((i - 14) % 6 + 4);
				s.setY(1);
			}
			StudentRepository.save(s);
		}
		System.out.println("Init students");

//		String[][] curriculum = { { "语文", "数学", "英语", "品德", "科学", "自习", "自习" },
//				{ "数学", "数学", "语文", "自习", "品德", "自习", "英语" }, { "语文", "语文", "数学", "数学", "英语", "英语", "自习" },
//				{ "英语", "英语", "语文", "数学", "科学", "自习", "自习" }, { "英语", "数学", "语文", "语文", "自习", "自习", "自习" },
//				{ "语文", "数学", "英语", "英语", "自习", "科学", "品德" }, { "语文", "数学", "英语", "自习", "自习", "自习", "自习" },
//				{ "语文", "数学", "英语", "自习", "自习", "自习", "自习" } };
//
//		String[] time = { "08:00-08:50", "09:00-09:50", "10:00-10:50", "11:00-11:50", "13:00-13:50", "14:00-14:50",
//				"15:00-23:59" };
//		int total_time = 50;
//
//		for (int i = 0; i < time.length; i++) {
//			Time t = new Time();
//			t.setId(i + 1);
//			t.setTime(time[i]);
//			t.setCid(cid);
//			t.setTotal_time(total_time);
//			TimeRepository.save(t);
//		}
//		try {
//			String endDate = DateUtil.getDate();
//			for (int i = 0; i < 7; i++) {
//				for (int j = 0; j < time.length; j++) {
//					String date = DateUtil.getPassedDate(i, endDate);
//					Curriculum c = new Curriculum();
//					c.setCid(cid);
//					c.setDay(DateUtil.dayForWeek(date));
//					c.setTid(j + 1);
//					c.setCourse(curriculum[i][j]);
//					c.setDate(date);
//					CurriculumRepository.save(c);
//					System.out.println("Init curriculum");
//				}
//			}
//		} catch (Exception e) {
//
//		}

//		String endDate = DateUtil.getDate();
//		for (int j = 0; j <= week; j++) {
//			String date = DateUtil.getPassedDate(j, endDate);
//			try {
//				int day = DateUtil.dayForWeek(date);
//				if (day == 6 || day == 7) {
//					continue;
//				} else {
//					for (int k = 0; k < curriculum[day].length; k++) {
//						for (int n = 0; n < student.length; n++) {
//							for (int m = 0; m < max_frequency; m++) {
//								double random = Math.random();
//								if (random < 0.95 && !curriculum[day - 1][k].equals("自习")) {
//									Behavior b = new Behavior();
//									b.setCid(cid);
//									b.setPlace(curriculum[day - 1][k]);
//									b.setDate(date);
//									b.setTid(k + 1);
//									b.setSid(n + 1);
//									if (random > 0.2)
//										b.setAction("举手");
//									else
//										b.setAction("回答问题");
//									BehaviorRepository.save(b);
//								}
//							}
//						}
//					}
//				}
//
//				System.out.println("Init");
//			} catch (Exception e) {
//
//			}
//
//		}
//		System.out.println("Init behavior");
	}

}
