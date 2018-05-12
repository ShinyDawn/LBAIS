package process.service;

import process.model.ClassBehaviorConfig;

public interface InitService {
	
	public void init(ClassBehaviorConfig config);
	
	public SourceService initSource();
	
	public PoseService initPose();
	
	public AnalyseService initProcessor();
	
	public AnalyseService initDebugProcessor();
}
