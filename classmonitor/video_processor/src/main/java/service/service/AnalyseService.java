package service.service;

import java.util.List;
import java.util.Observer;

import service.model.Pose;
import service.model.StudentPose;
import service.repository.BehaviorRepository;
import service.repository.StudentRepository;

public interface AnalyseService {

	public double analyse(List<StudentPose> list, String course, int classroom, Pose[][] behavior, Observer observer,
			BehaviorRepository behaviorRepo, StudentRepository studentRepo);
}
