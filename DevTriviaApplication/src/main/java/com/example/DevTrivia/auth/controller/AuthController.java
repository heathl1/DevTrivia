package com.example.DevTrivia.auth.controller;

import com.example.DevTrivia.auth.dto.PasswordResetForm;
import com.example.DevTrivia.auth.dto.RegistrationForm;
import com.example.DevTrivia.auth.model.User;
import com.example.DevTrivia.auth.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class AuthController {

    private final UserService userService;

    // Keep questions in one place so Register + Reset always match
    private static final String[] SECURITY_QUESTIONS = new String[]{
            "What is your favorite teacher's last name?",
            "What city were you born in?",
            "What was your first pet's name?"
    };

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String getRegister(Model model) {
        model.addAttribute("form", new RegistrationForm());
        model.addAttribute("questions", SECURITY_QUESTIONS);
        return "register";
    }

    @PostMapping("/register")
    public String postRegister(@Valid @ModelAttribute("form") RegistrationForm form,
                               BindingResult binding, Model model) {

        if (binding.hasErrors()) {
            model.addAttribute("questions", SECURITY_QUESTIONS);
            return "register";
        }

        try {
            userService.register(form);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("message", ex.getMessage());
            model.addAttribute("questions", SECURITY_QUESTIONS);
            return "register";
        }

        return "redirect:/login?registered";
    }

    @GetMapping("/reset")
    public String getReset(Model model) {
        model.addAttribute("form", new PasswordResetForm());
        model.addAttribute("questions", SECURITY_QUESTIONS);
        return "reset";
    }

    @PostMapping("/reset")
    public String postReset(@Valid @ModelAttribute("form") PasswordResetForm form,
                            BindingResult binding, Model model) {

        // Always re-add questions if we return the same page
        model.addAttribute("questions", SECURITY_QUESTIONS);

        if (binding.hasErrors()) return "reset";

        Optional<User> opt = userService.findByUsername(form.getUsername());
        if (opt.isEmpty()) {
            model.addAttribute("message", "Unknown username.");
            return "reset";
        }

        User user = opt.get();

        if (!user.getSecurityQuestion().equals(form.getSecurityQuestion())) {
            model.addAttribute("message", "Security question mismatch. Please select the same question you chose during account creation.");
            return "reset";
        }

        if (!userService.verifySecurityAnswer(user, form.getSecurityAnswer())) {
            model.addAttribute("message", "Incorrect security answer.");
            return "reset";
        }

        userService.updatePassword(user, form.getNewPassword());
        return "redirect:/login?reset";
    }

    @GetMapping("/")
    public String home(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "index";
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equals);

        if (isAdmin) return "redirect:/admin";

        return "index";
    }

    @GetMapping("/account")
    public String account() {
        return "account";
    }

    @GetMapping("/game")
    public String game() {
        return "game";
    }
}