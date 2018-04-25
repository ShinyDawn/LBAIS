package presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ExceptionController {
	
	@RequestMapping(value = "/exception")
	public String index() {
	    return "teacher/exception";
	}
}
