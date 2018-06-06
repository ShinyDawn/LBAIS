package service.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import service.model.Pose;
import service.model.RaiseHandStudent;
import service.service.BehaviorService;
import service.service.ListenerService;

@RestController
public class ClassBehaviorController{
	@Autowired
	private BehaviorService behaviorService;
	@Autowired
	private ListenerService listener;

	@RequestMapping(value = "/realTime/focus")
	public double[][] index() {
		Pose[][] pose = behaviorService.getPose();

		double[][] list = new double[pose.length][pose[0].length];

		boolean isClass = false;

		for (int i = 0; i < pose.length; i++) {
			for (int j = 0; j < pose[i].length; j++) {
				Pose p = pose[i][j];
				if (p.getFocus() != 0) {
					isClass = true;
					break;
				}
			}
		}
		if (!isClass)
			return null;

		for (int i = 0; i < pose.length; i++) {
			for (int j = 0; j < pose[i].length; j++) {
				Pose p = pose[i][j];
				list[i][j] = p.getFocus();
			}
		}
		return list;
	}

	@RequestMapping(value = "/realTime/raise")
	public ArrayList<RaiseHandStudent> raise() throws InterruptedException {
		while (listener.getMessage() == 0) {
			Thread.sleep(10000);
		}
		return listener.getRaiseHandStudent();
	}
}
