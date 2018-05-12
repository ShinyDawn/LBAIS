package process.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import process.model.StudentPose;
import process.service.PoseService;
import process.tool.JsonParser;
import service.tool.IOHelper;

public class ClassBehaviorGetter implements PoseService {

	@Override
	public List<StudentPose> getStudentPose(String path) {
		List<StudentPose> list = new ArrayList<StudentPose>();
		
		try {
			IOHelper ioHelper = new IOHelper();
			BufferedReader br = ioHelper.read(path);
			
			StringBuffer str = new StringBuffer();
			String line = "";
			while ((line = br.readLine()) != null) {
				str.append(line);
			}
			
			JsonParser parser = new JsonParser();
			list = parser.readJson(str.toString());
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
}
