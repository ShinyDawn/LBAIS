package service.impl;

import service.model.ClassBehaviorConfig;
import service.service.AnalyseService;
import service.service.InitService;
import service.service.PoseService;
import service.service.SourceService;

public class InitImpl implements InitService{
	private ClassBehaviorConfig config;

	@Override
	public void init(ClassBehaviorConfig config) {
		this.config = config;
	}
	
	@Override
	public SourceService initSource() {
		String source = config.getSource();
		try {
			SourceService service;
			service = (SourceService)Class.forName(source).newInstance();
			return service;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public PoseService initPose() {
		String sourceProducer = config.getPose();
		try {
			PoseService service;
			service = (PoseService)Class.forName(sourceProducer).newInstance();
			return service;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public AnalyseService initProcessor() {
		String processor = config.getProcessor();
		try {
			AnalyseService service;
			service = (AnalyseService)Class.forName(processor).newInstance();
			return service;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public AnalyseService initDebugProcessor() {
		String debug = config.getDebugProcessor();
		try {
			AnalyseService service;
			service = (AnalyseService)Class.forName(debug).newInstance();
			return service;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
