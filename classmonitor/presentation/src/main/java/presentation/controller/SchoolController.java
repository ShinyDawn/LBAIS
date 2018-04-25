package presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SchoolController {

	@RequestMapping(value = "/class")
    public String index() {
        return "school/classes";
    }
	
	@RequestMapping(value = "/ss")
    public String schoolStatictics() {
        return "school/school_statistics";
    }
	
	@RequestMapping(value = "/number")
    public String number() {
        return "school/number";
    }
	
	@RequestMapping(value = "/help")
    public String help() {
        return "school/help";
    }
}
