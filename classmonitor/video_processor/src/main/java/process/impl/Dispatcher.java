package process.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import process.model.StudentPose;
import process.service.AnalyseService;
import process.service.DispatchService;
import process.service.PoseService;
import process.service.SourceService;
import service.entity.Time;
import service.repository.TimeRepository;

public class Dispatcher implements DispatchService{
	@Autowired
	TimeRepository timeRepo;
	
	private static List<Time> list;

	@Override
	public void init(int classroom) {
		list = timeRepo.findByCid(classroom);
		//todo 获得课程表
	}

	@Override
	public void dispatch(SourceService source, PoseService pose, AnalyseService classBehavior,
			AnalyseService selfStudy) {
		//不仅要time，还要对应的课程名称判断是否上课
		String filename = source.getNextSource();
		List<StudentPose> list = pose.getStudentPose(filename);
		classBehavior.analyse(list);
	}
}
