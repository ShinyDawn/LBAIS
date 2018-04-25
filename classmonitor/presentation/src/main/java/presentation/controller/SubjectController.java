package presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SubjectController {
	
	@RequestMapping(value = "/subject")
    public String index() {
        return "teacher/subject_list";
    }

	@RequestMapping(value = "/single")
    public String subject() {
        return "teacher/subject";
    }
}
