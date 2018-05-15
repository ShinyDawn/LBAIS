package service.service;

import java.util.Observer;

import service.model.Pose;
import service.repository.BehaviorRepository;
import service.repository.CurriculumRepository;
import service.repository.StudentRepository;
import service.repository.TimeRepository;

public interface DispatchService {

	public void init(int cid, TimeRepository timeRepo, CurriculumRepository curriculum, BehaviorRepository behavior,
			StudentRepository studentRepo, Observer observer);

	public void dispatch(SourceService source, PoseService pose, AnalyseService classBehavior);

	public Pose[][] getPose();
}
