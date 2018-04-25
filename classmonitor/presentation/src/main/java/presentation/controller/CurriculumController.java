package presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CurriculumController {

	@RequestMapping(value = "/curriculum")
    public String getTestInfo() {
        return "teacher/curriculum";
    }
}
