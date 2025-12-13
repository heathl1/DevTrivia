package com.example.DevTrivia.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

 @Controller
public class AuthPagesController {


    @GetMapping("/login")
    public String loginPage(
            @RequestParam(value = "disabled", required = false) String disabled,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "error", required = false) String error,
            Model model
    ) {
        model.addAttribute("disabled", disabled != null);
        model.addAttribute("logout", logout != null);
        model.addAttribute("error", error != null);
        return "login";
    }

}
