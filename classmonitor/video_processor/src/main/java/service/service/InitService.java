package service.service;

import service.model.ClassBehaviorConfig;

public interface InitService {
	
	public void init(ClassBehaviorConfig config);
	
	public SourceService initSource();
	
	public PoseService initPose();
	
	public AnalyseService initProcessor();
	
	public AnalyseService initDebugProcessor();
}
