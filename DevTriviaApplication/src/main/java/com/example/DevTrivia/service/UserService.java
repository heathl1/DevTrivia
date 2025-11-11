package com.example.DevTrivia.service;

import com.example.DevTrivia.dto.RegistrationForm;
import com.example.DevTrivia.model.User;
import com.example.DevTrivia.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.userRepository = repo;
        this.passwordEncoder = encoder;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean usernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean emailTaken(String email) {
        return email != null && !email.isBlank() && userRepository.existsByEmail(email);
    }

    @Transactional
    public User register(RegistrationForm form) {
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match.");
        }
        if (usernameTaken(form.getUsername())) {
            throw new IllegalArgumentException("Username is already taken.");
        }
        if (form.getEmail() != null && !form.getEmail().isBlank() && emailTaken(form.getEmail())) {
            throw new IllegalArgumentException("Email is already used.");
        }

        User u = new User();
        u.setUsername(form.getUsername().trim());
        u.setEmail(form.getEmail() == null ? null : form.getEmail().trim());
        u.setSecurityQuestion(form.getSecurityQuestion());
        u.setPasswordHash(passwordEncoder.encode(form.getPassword()));
        u.setSecurityAnswerHash(passwordEncoder.encode(form.getSecurityAnswer()));

        return userRepository.save(u);
    }

    public boolean verifyPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPasswordHash());
    }

    public boolean verifySecurityAnswer(User user, String rawAnswer) {
        return passwordEncoder.matches(rawAnswer, user.getSecurityAnswerHash());
    }

    @Transactional
    public void updatePassword(User user, String newPassword) {
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
