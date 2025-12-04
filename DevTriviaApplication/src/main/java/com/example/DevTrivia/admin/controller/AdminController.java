package com.example.DevTrivia.admin.controller;

import com.example.DevTrivia.auth.model.User;
import com.example.DevTrivia.auth.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    /** Simple admin landing page. */
    @GetMapping
    public String dashboard() {
        return "admin/dashboard";
    }

    /** List users + show the create-user form. */
    @GetMapping("/users")
    public String listUsers(Model model) {
        if (!model.containsAttribute("createForm")) {
            model.addAttribute("createForm", new CreateUserForm());
        }

        model.addAttribute("users", userRepo.findAll());
        return "admin/users";
    }

    /** Handle create-user form submit. */
    @PostMapping("/users")
    public String createUser(
            @Valid @ModelAttribute("createForm") CreateUserForm form,
            BindingResult bindingResult,
            Model model
    ) {
        if (userRepo.existsByUsername(form.getUsername())) {
            bindingResult.rejectValue("username", "duplicate", "Username already exists.");
        }
        if (userRepo.existsByEmail(form.getEmail())) {
            bindingResult.rejectValue("email", "duplicate", "Email already exists.");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userRepo.findAll());
            return "admin/users";
        }

        User u = new User();
        u.setUsername(form.getUsername());
        u.setEmail(form.getEmail());
        u.setPasswordHash(passwordEncoder.encode(form.getPassword()));

        // admin / enabled flags
        u.setAdmin(form.isAdmin());
        u.setEnabled(form.isEnabled());  // defaults to true in the form

        // stats / metadata
        u.setJoinDate(Instant.now());
        u.setTotalCorrect(0);
        u.setGamesPlayed(0);
        // securityQuestion / Answer left null by default

        userRepo.save(u);
        return "redirect:/admin/users";
    }

    /** Show edit form for a specific user. */
    @GetMapping("/users/{id}/edit")
    public String editUserForm(@PathVariable Long id, Model model) {
        Optional<User> opt = userRepo.findById(id);
        if (opt.isEmpty()) {
            return "redirect:/admin/users";
        }

        User u = opt.get();
        EditUserForm form = new EditUserForm();
        form.setId(u.getId());
        form.setUsername(u.getUsername());
        form.setEmail(u.getEmail());
        form.setAdmin(u.isAdmin());
        form.setEnabled(u.isEnabled());

        model.addAttribute("form", form);
        return "admin/user_edit";
    }

    /** Handle edit form submit. */
    @PostMapping("/users/{id}/edit")
    public String editUser(
            @PathVariable Long id,
            @Valid @ModelAttribute("form") EditUserForm form,
            BindingResult bindingResult,
            Model model
    ) {
        Optional<User> opt = userRepo.findById(id);
        if (opt.isEmpty()) {
            return "redirect:/admin/users";
        }

        if (bindingResult.hasErrors()) {
            return "admin/user_edit";
        }

        User u = opt.get();
        u.setUsername(form.getUsername());
        u.setEmail(form.getEmail());
        u.setAdmin(form.isAdmin());
        u.setEnabled(form.isEnabled());

        if (form.getNewPassword() != null && !form.getNewPassword().isBlank()) {
            u.setPasswordHash(passwordEncoder.encode(form.getNewPassword()));
        }

        userRepo.save(u);
        return "redirect:/admin/users";
    }

    // -------------------------------------------------------------------------
    // Inner form classes for Create and Edit
    // -------------------------------------------------------------------------

    /** Form used on the /admin/users page when creating a new user. */
    public static class CreateUserForm {
        @NotBlank
        private String username;

        @NotBlank
        @Email
        private String email;

        @NotBlank
        private String password;

        private boolean admin;

        // new field: default true so new users start enabled
        private boolean enabled = true;

        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }

        public boolean isAdmin() {
            return admin;
        }
        public void setAdmin(boolean admin) {
            this.admin = admin;
        }

        public boolean isEnabled() {
            return enabled;
        }
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    /** Form used on the /admin/users/{id}/edit page. */
    public static class EditUserForm {
        private Long id;

        @NotBlank
        private String username;

        @NotBlank
        @Email
        private String email;

        private boolean admin;

        // enabled toggle for editing
        private boolean enabled;

        private String newPassword;  // optional

        public Long getId() {
            return id;
        }
        public void setId(Long id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }

        public boolean isAdmin() {
            return admin;
        }
        public void setAdmin(boolean admin) {
            this.admin = admin;
        }

        public boolean isEnabled() {
            return enabled;
        }
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getNewPassword() {
            return newPassword;
        }
        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }
}
