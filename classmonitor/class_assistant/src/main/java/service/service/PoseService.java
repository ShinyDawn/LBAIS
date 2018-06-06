package service.service;

import java.util.List;

import service.model.StudentPose;

public interface PoseService {
	
	public List<StudentPose> getStudentPose(String path);
}
