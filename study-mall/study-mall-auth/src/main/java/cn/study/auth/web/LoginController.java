package cn.study.auth.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {


    @GetMapping("login.html")
    public String loginPage(){
        return "login";
    }

    @GetMapping("reg.html")
    public String regPage(){
        return "reg";
    }
}
