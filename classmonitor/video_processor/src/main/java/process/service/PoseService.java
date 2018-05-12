package process.service;

import java.util.List;

import process.model.StudentPose;

public interface PoseService {
	
	public List<StudentPose> getStudentPose(String path);
}
