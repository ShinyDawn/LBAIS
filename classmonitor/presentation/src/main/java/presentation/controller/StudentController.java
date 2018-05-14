package presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StudentController {

	@RequestMapping(value = "/students")
    public String index() {
        return "students";
    }

    @RequestMapping(value = "/student")
    public String getDetail() {
        return "studentsDetail";
    }
}
