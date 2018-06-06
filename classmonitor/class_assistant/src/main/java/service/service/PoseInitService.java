package service.service;

import java.util.List;

import service.model.Pose;
import service.model.StudentPose;

public interface PoseInitService {

	public Pose[][] init(List<StudentPose> list, Pose[][] pose);
}
