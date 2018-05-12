package service.impl;

import java.util.List;

import service.entity.Time;
import service.model.StudentPose;
import service.repository.BehaviorRepository;
import service.repository.CurriculumRepository;
import service.repository.TimeRepository;
import service.service.AnalyseService;
import service.service.DispatchService;
import service.service.PoseService;
import service.service.SourceService;

public class Dispatcher implements DispatchService {
	private static TimeRepository timeRepo;
	private static CurriculumRepository curriculumRepo;
	private static BehaviorRepository behaviorRepo;
	private static List<Time> list;

	@Override
	public void init(int classroom, TimeRepository time, CurriculumRepository curriculum,
			BehaviorRepository behavior) {
		timeRepo = time;
		curriculumRepo = curriculum;
		behaviorRepo = behavior;
		System.out.println(timeRepo);
		list = timeRepo.findByCid(classroom);
		// todo 获得课程表
	}

	@Override
	public void dispatch(SourceService source, PoseService pose, AnalyseService classBehavior) {
		// 不仅要time，还要对应的课程名称判断是否上课
		String filename = source.getNextSource();
		List<StudentPose> list = pose.getStudentPose(filename);
		classBehavior.analyse(list);
	}
}
