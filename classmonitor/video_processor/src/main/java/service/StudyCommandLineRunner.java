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
		study.checkPattern();
		System.out.println("This will be execute when the project was started!");
		// TODO Auto-generated method stub
		
	}

}
