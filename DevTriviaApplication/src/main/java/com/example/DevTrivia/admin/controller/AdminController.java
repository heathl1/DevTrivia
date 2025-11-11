package com.example.DevTrivia.admin.controller;

import com.example.DevTrivia.auth.model.User;
import com.example.DevTrivia.auth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /** /admin -> redirect to /admin/users */
    @GetMapping("")
    public String adminHome() {
        return "redirect:/admin/users";
    }

    /** List users + create form */
    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userRepo.findAll());
        model.addAttribute("createForm", new CreateUserForm());
        return "admin/users"; // templates/admin/users.html
    }

    /** Handle create user */
    @PostMapping("/users")
    public String createUser(@ModelAttribute("createForm") CreateUserForm form,
                             BindingResult binding, Model model) {
        if (binding.hasErrors()) {
            model.addAttribute("users", userRepo.findAll());
            return "admin/users";
        }

        // minimal duplicate check
        if (userRepo.findByUsername(form.getUsername()).isPresent()) {
            binding.rejectValue("username", "dup", "Username already exists");
            model.addAttribute("users", userRepo.findAll());
            return "admin/users";
        }

        User u = new User();
        u.setUsername(form.getUsername());
        u.setEmail(form.getEmail());
        u.setAdmin(form.isAdmin());                 // requires setAdmin(boolean) on User
        u.setJoinDate(Instant.now());
        if (form.getPassword() != null && !form.getPassword().isBlank()) {
            u.setPasswordHash(passwordEncoder.encode(form.getPassword()));
        }

        userRepo.save(u);
        return "redirect:/admin/users";
    }

    /** Edit screen */
    @GetMapping("/users/{id}/edit")
    public String editUser(@PathVariable Long id, Model model) {
        Optional<User> opt = userRepo.findById(id);
        if (opt.isEmpty()) return "redirect:/admin/users";
        User u = opt.get();

        EditUserForm f = new EditUserForm();
        f.setId(u.getId());
        f.setUsername(u.getUsername());
        f.setEmail(u.getEmail());
        f.setAdmin(Boolean.TRUE.equals(u.getIsAdmin()));

        model.addAttribute("form", f);
        return "admin/user-edit"; // templates/admin/user-edit.html
    }

    /** Save edits */
    @PostMapping("/users/{id}/edit")
    public String updateUser(@PathVariable Long id,
                             @ModelAttribute("form") EditUserForm form,
                             BindingResult binding) {
        Optional<User> opt = userRepo.findById(id);
        if (opt.isEmpty()) return "redirect:/admin/users";

        User u = opt.get();
        u.setUsername(form.getUsername());
        u.setEmail(form.getEmail());
        u.setAdmin(form.isAdmin());

        if (form.getNewPassword() != null && !form.getNewPassword().isBlank()) {
            u.setPasswordHash(passwordEncoder.encode(form.getNewPassword()));
        }

        userRepo.save(u);
        return "redirect:/admin/users";
    }

    /** Delete */
    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        if (userRepo.existsById(id)) {
            userRepo.deleteById(id);
        }
        return "redirect:/admin/users";
    }

    /* ---------- Simple DTOs for forms ---------- */

    public static class CreateUserForm {
        @NotBlank private String username;
        @Email   private String email;
        private String password;
        private boolean admin;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public boolean isAdmin() { return admin; }
        public void setAdmin(boolean admin) { this.admin = admin; }
    }

    public static class EditUserForm {
        private Long id;
        @NotBlank private String username;
        @Email   private String email;
        private boolean admin;
        private String newPassword; // optional

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public boolean isAdmin() { return admin; }
        public void setAdmin(boolean admin) { this.admin = admin; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }
}
