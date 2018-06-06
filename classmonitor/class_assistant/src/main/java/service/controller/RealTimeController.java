package service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RealTimeController {

	@RequestMapping(value = "/realtime")
    public String index() {
        return "realtime";
    }
}
