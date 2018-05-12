package service.impl;

import java.util.List;

import service.model.StudentPose;
import service.service.AnalyseService;

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
