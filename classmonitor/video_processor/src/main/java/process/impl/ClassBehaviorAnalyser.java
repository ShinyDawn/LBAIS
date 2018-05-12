package process.impl;

import java.util.List;

import process.model.StudentPose;
import process.service.AnalyseService;

public class ClassBehaviorAnalyser implements AnalyseService{
	private static double[][] focus;

	@Override
	public void init(int classroom) {
		
	}
	
	@Override
	public void analyse(List<StudentPose> list) {
		System.out.println(list.get(0).getNose().getX());
	}
}
