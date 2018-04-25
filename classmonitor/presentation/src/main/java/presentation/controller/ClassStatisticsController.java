package presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ClassStatisticsController {

	@RequestMapping(value = "/cs")
    public String index() {
        return "teacher/class_statistics";
    }
}
