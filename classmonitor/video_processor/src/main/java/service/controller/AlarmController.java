package service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import service.impl.SelfstudyImpl;
import service.service.SelfstudyService;

@Controller
public class AlarmController {
	
	@Autowired
	private SelfstudyService selfstudy;

	@RequestMapping(value = "/alarm")
    public void index() throws Exception {
		SelfstudyImpl.sendPost("http://localhost:10002/study/alertAlarm?alarm_id=1","");
    }
	
	@RequestMapping(value = "/selfstudy")
	public String selfstudy() throws Exception {
		selfstudy.test1960();
		return "selfstudy";
    }
}
