package service.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import service.service.LoginService;

@Controller
public class LoginController {
    @Autowired
    private LoginService login;

    @RequestMapping(value = "/login")
    @ResponseBody
    public String check(@RequestParam("uid") String uid, @RequestParam("password") String password) {

        return login.Check(uid, password);
    }

}
