package service.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import service.model.ClassBehaviorConfig;
import service.tool.IOHelper;
import service.tool.PathConfig;

public class ConfigInit {
	private String config = "config.conf";

	public ClassBehaviorConfig config() {
		IOHelper ioHelper = new IOHelper();
		BufferedReader br = ioHelper.read(PathConfig.baseDir + config);
		ArrayList<String> confList = new ArrayList<String>();
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				if (!line.equals(""))
					confList.add(line);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ClassBehaviorConfig c = new ClassBehaviorConfig();
		for (String conf : confList) {
			parser(conf, c);
		}

		return c;
	}

	private String parser(String text, ClassBehaviorConfig c) {
		String[] content = text.split("=");
		switch (content[0]) {
		case "source_dir":
			c.setDir(content[1]);
			break;
		case "target":
			c.setTarget(content[1]);
			break;
		case "pose":
			c.setPose(content[1]);
			break;
		case "source":
			c.setSource(content[1]);
			break;
		case "processor":
			c.setProcessor(content[1]);
			break;
		case "debug_processor":
			c.setDebugProcessor(content[1]);
			break;
		case "debug":
			c.setDebug(content[1]);
			break;
		case "interval":
			c.setInterval(Double.parseDouble(content[1]));
			break;
		case "classroom":
			c.setClassroom(Integer.parseInt(content[1]));
			break;
		default:
			break;
		}

		return text;
	}
}
