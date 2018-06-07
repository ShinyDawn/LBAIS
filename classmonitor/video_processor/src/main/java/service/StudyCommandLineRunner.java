package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import service.entity.Behavior;
import service.repository.BehaviorRepository;
import service.service.SelfstudyService;

@Component
public class StudyCommandLineRunner implements CommandLineRunner{
	@Autowired
	private SelfstudyService study;
	
	@Autowired
	private BehaviorRepository behavior;
	
	@Override
	public void run(String... args) throws Exception {
//		behavior.updateById(3, "08:45:00", "已请假", "缺勤", 2700);
//		Behavior temp=new Behavior();
//		temp.setId(0);
//		behavior.save(temp);
//		System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
		//study.takeClass("D:\\workspace\\video\\jsonOut\\IMG_1961",2,"../video/alarm2.mp4");
		//study.checkPattern(2);
		//System.out.println("This will be execute when the project was started!");
		
	}

}
