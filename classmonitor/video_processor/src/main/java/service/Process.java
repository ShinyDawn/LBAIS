package service;

import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import service.impl.Dispatcher;
import service.impl.InitImpl;
import service.model.ClassBehaviorConfig;
import service.repository.BehaviorRepository;
import service.repository.CurriculumRepository;
import service.repository.TimeRepository;
import service.service.AnalyseService;
import service.service.DispatchService;
import service.service.InitService;
import service.service.PoseService;
import service.service.SourceService;
import service.tool.ConfigInit;

@Component
public class Process implements CommandLineRunner {
	@Autowired
	private TimeRepository timeRepo;
	@Autowired
	private CurriculumRepository curriculumRepo;
	@Autowired
	private BehaviorRepository behaviorRepo;

	private static SourceService source = null;
	private static PoseService pose = null;
	private static AnalyseService analyseClassBehavior = null;
	private static ClassBehaviorConfig conf = null;
	private static DispatchService dispatcher;

	public static void timer() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				dispatcher.dispatch(source, pose, analyseClassBehavior);
			}
		}, 1000, (long) (conf.getInterval() * 1000));
	}

	@Override
	public void run(String... args) throws Exception {
		ConfigInit configInit = new ConfigInit();
		conf = configInit.config();

		InitService init = new InitImpl();
		init.init(conf);

		dispatcher = new Dispatcher();
		dispatcher.init(conf.getClassroom(), timeRepo, curriculumRepo, behaviorRepo);
		source = init.initSource();
		source.init(conf.getDir(), conf.getTarget());
		pose = init.initPose();
		analyseClassBehavior = null;

		if (conf.getDebug().equals("true"))
			analyseClassBehavior = init.initDebugProcessor();
		else
			analyseClassBehavior = init.initProcessor();
		timer();
	}
}
