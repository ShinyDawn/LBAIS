package process.service;

import java.util.List;

import process.model.StudentPose;

public interface AnalyseService {

	public void init(int classroom);
	
	public void analyse(List<StudentPose> list);
}
