package service.service;

import service.repository.BehaviorRepository;
import service.repository.CurriculumRepository;
import service.repository.TimeRepository;

public interface DispatchService {

	public void init(int classroom, TimeRepository timeRepo, CurriculumRepository curriculumRepo,
			BehaviorRepository behaviorRepo);

	public void dispatch(SourceService source, PoseService pose, AnalyseService classBehavior);
}
