package service.impl;

import java.util.List;
import java.util.Observer;

import service.model.Pose;
import service.model.StudentPose;
import service.repository.BehaviorRepository;
import service.repository.StudentRepository;
import service.service.AnalyseService;

public class ClassBehaviorDebugger implements AnalyseService {

	@Override
	public double analyse(List<StudentPose> list, String course, int classroom, Pose[][] behavior, Observer observer,
			BehaviorRepository behaviorRepo, StudentRepository studentRepo) {
		// TODO Auto-generated method stub
		return 0;
	}
}
