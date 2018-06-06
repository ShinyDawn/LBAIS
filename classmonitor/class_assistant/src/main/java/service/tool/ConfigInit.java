package service.tool;

import java.io.BufferedReader;
import java.io.IOException;

import service.model.ClassBehaviorConfig;

public class ConfigInit {
	private static final String filename = "config.conf";

	public ClassBehaviorConfig config() {
		IOHelper ioHelper = new IOHelper();
		BufferedReader br = ioHelper.read(PathConfig.baseDir + filename);

		String str = null;
		ClassBehaviorConfig conf = new ClassBehaviorConfig();

		try {
			while ((str = br.readLine()) != null) {
				if (!str.startsWith("#") && str.contains("=")) {
					String[] text = str.split("=");
					setConfig(conf, text[0], text[1]);
				}
			}
			br.close();
		} catch (IOException e) {

		}

		return conf;
	}

	private void setConfig(ClassBehaviorConfig conf, String name, String value) {
		switch (name) {
		case "source_dir":
			conf.setDir(value);
			break;
		case "target":
			conf.setTarget(value);
			break;
		case "source":
			conf.setSource(value);
			break;
		case "pose":
			conf.setPose(value);
			break;
		case "processor":
			conf.setProcessor(value);
			break;
		case "debug":
			conf.setDebug(value);
			break;
		case "debug_processor":
			conf.setDebugProcessor(value);
			break;
		case "interval":
			conf.setInterval(Double.parseDouble(value));
			break;
		case "classroom":
			conf.setClassroom(Integer.parseInt(value));
			break;
		default:
			break;
		}
	}
}
