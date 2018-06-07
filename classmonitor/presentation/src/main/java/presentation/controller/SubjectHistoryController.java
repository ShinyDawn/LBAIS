package presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SubjectHistoryController {
	
	@RequestMapping(value = "/subjectHistory")
	public String index() {
	    return "subjectHistory";
	}
}
