package presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by elva on 2018/5/13.
 */
@Controller
public class ApprovalController {
    @RequestMapping(value = "/approval")
    public String index() {
        return "approval";
    }
}
