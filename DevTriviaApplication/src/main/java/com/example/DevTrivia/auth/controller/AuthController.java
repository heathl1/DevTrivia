package com.example.DevTrivia.auth.controller;

import com.example.DevTrivia.auth.dto.LoginForm;
import com.example.DevTrivia.auth.dto.PasswordResetForm;
import com.example.DevTrivia.auth.dto.RegistrationForm;
import com.example.DevTrivia.auth.model.User;
import com.example.DevTrivia.auth.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AuthController {

    private final UserService userService;
    public AuthController(UserService userService) { this.userService = userService; }

    @GetMapping("/login")
    public String getLogin(Model model, @RequestParam(value = "error", required = false) String error) {
        model.addAttribute("form", new LoginForm());
        if (error != null) model.addAttribute("message", "Invalid username or password.");
        return "login";
    }

    @GetMapping("/register")
    public String getRegister(Model model) {
        model.addAttribute("form", new RegistrationForm());
        model.addAttribute("questions", new String[]{
                "What is your favorite teacher's last name?",
                "What city were you born in?",
                "What was your first pet's name?"
        });
        return "register";
    }

    @PostMapping("/register")
    public String postRegister(@Valid @ModelAttribute("form") RegistrationForm form,
                               BindingResult binding, Model model) {
        if (binding.hasErrors()) {
            model.addAttribute("questions", new String[]{
                    "What is your favorite teacher's last name?",
                    "What city were you born in?",
                    "What was your first pet's name?"
            });
            return "register";
        }
        try {
            userService.register(form);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("message", ex.getMessage());
            model.addAttribute("questions", new String[]{
                    "What is your favorite teacher's last name?",
                    "What city were you born in?",
                    "What was your first pet's name?"
            });
            return "register";
        }
        return "redirect:/login?registered";
    }

    @GetMapping("/reset")
    public String getReset(Model model) {
        model.addAttribute("form", new PasswordResetForm());
        return "reset";
    }

    @PostMapping("/reset")
    public String postReset(@Valid @ModelAttribute("form") PasswordResetForm form,
                            BindingResult binding, Model model) {
        if (binding.hasErrors()) return "reset";

        Optional<User> opt = userService.findByUsername(form.getUsername());
        if (opt.isEmpty()) {
            model.addAttribute("message", "Unknown username.");
            return "reset";
        }
        User user = opt.get();
        if (!user.getSecurityQuestion().equals(form.getSecurityQuestion())) {
            model.addAttribute("message", "Security question mismatch.");
            return "reset";
        }
        if (!userService.verifySecurityAnswer(user, form.getSecurityAnswer())) {
            model.addAttribute("message", "Incorrect security answer.");
            return "reset";
        }
        userService.updatePassword(user, form.getNewPassword());
        return "redirect:/login?reset";
    }

    // Guest mode — mark session as guest and redirect
    @PostMapping("/guest")
    public String guest(HttpSession session) {
        session.setAttribute("guest", true);
        return "redirect:/";
    }
}
