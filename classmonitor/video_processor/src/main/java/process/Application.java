package process;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import process.impl.InitImpl;
import process.model.ClassBehaviorConfig;
import process.model.StudentPose;
import process.service.AnalyseService;
import process.service.InitService;
import process.service.PoseService;
import process.service.SourceService;
import process.tool.ConfigInit;

//生成视频文件的首帧为图片
//windows下的版本
public class Application {
	private static SourceService source = null;
	private static PoseService pose = null;
	private static AnalyseService analyseClassBehavior = null;
	private static ClassBehaviorConfig conf = null;

	public static void main(String[] args) {

		ConfigInit configInit = new ConfigInit();
		conf = configInit.config();

		InitService init = new InitImpl();
		init.init(conf);

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

	public static void timer() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				String filename = source.getNextSource();
				List<StudentPose> list = pose.getStudentPose(filename);
				analyseClassBehavior.analyse(list);
			}
		}, 1000, (long) (conf.getInterval() * 1000));
	}
}
