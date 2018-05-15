package presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StudyController {
	
	@RequestMapping(value = "/study")
    public String index() {
        return "prototype/study";
    }
	
	@RequestMapping(value = "/handle")
    public String handle() {
        return "prototype/handle";
    }
	
	@RequestMapping(value = "/alarm_layer")
    public String alarm_layer() {
        return "prototype/alarm";
    }
	
	@RequestMapping(value = "/handle_alarm")
    public String Halarm_layer() {
        return "prototype/handle_alarm";
    }
	
	@RequestMapping(value = "/handle_pattern")
    public String Hpattern_layer() {
        return "prototype/handle_pattern";
    }
	
	@RequestMapping(value = "/choose_stu")
    public String choose_stu() {
        return "prototype/choose_stu";
    }
}
