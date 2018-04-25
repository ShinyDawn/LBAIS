package process;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import process.model.Config;
import service.tool.IOHelper;

public class Configuration {
	private String config = "config.conf";

	public void Config() {
		IOHelper ioHelper = new IOHelper();
		BufferedReader br = ioHelper.read(config);
		ArrayList<String> confList = new ArrayList<String>();
		String line = null;
		try {
			line = br.readLine();
			while (line != null) {
				confList.add(line);
				line = br.readLine();
			}
			confList.add(line);
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Config c = new Config();
		for (String conf : confList) {
			parser(conf, c);
		}
	}

	public String parser(String text, Config c) {
		String[] content = text.split("=");
		switch (content[0]) {
		case "source_dir":
			c.setDir(content[1]);
			break;
		case "source_producer":
			c.setSourceProducer(content[1]);
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
		case "start":
			c.setStart(Integer.parseInt(content[1]));
			break;
		case "end":
			c.setEnd(Integer.parseInt(content[1]));
			break;
		default:
			break;
		}
		
		return text;
	}
}
