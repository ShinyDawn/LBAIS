package service.service;

import java.util.List;

import service.model.StudentPose;

public interface AnalyseService {

	public void init(int classroom);
	
	public void analyse(List<StudentPose> list);
}
