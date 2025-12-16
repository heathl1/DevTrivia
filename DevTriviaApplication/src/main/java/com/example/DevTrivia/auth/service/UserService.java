package com.example.DevTrivia.auth.service;

import com.example.DevTrivia.auth.dto.RegistrationForm;
import com.example.DevTrivia.auth.model.User;
import com.example.DevTrivia.auth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

// Service layer responsible for user-related business logic.
@Service
public class UserService {

    // Repository used to interact with the user table.
    private final UserRepository userRepository;

    // Encoder used to hash and verify passwords and security answers.
    private final PasswordEncoder passwordEncoder;

    // Constructor-based injection for required dependencies.
    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.userRepository = repo;
        this.passwordEncoder = encoder;
    }

    // Retrieves a user by username, commonly used during authentication.
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Checks if a username is already taken during registration.
    public boolean usernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }

    // Checks if an email is already in use, ignoring null or blank values.
    public boolean emailTaken(String email) {
        return email != null && !email.isBlank() && userRepository.existsByEmail(email);
    }

    // Handles new user registration and enforces basic validation rules.
    @Transactional
    public User register(RegistrationForm form) {

        // Ensures the user entered matching passwords.
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match.");
        }

        // Prevents duplicate usernames.
        if (usernameTaken(form.getUsername())) {
            throw new IllegalArgumentException("Username is already taken.");
        }

        // Prevents duplicate emails when one is provided.
        if (form.getEmail() != null && !form.getEmail().isBlank() && emailTaken(form.getEmail())) {
            throw new IllegalArgumentException("Email is already used.");
        }

        // Creates and populates a new User entity.
        User u = new User();
        u.setUsername(form.getUsername().trim());
        u.setEmail(form.getEmail() == null ? null : form.getEmail().trim());
        u.setSecurityQuestion(form.getSecurityQuestion());

        // Hashes sensitive values before storing them.
        u.setPasswordHash(passwordEncoder.encode(form.getPassword()));
        u.setSecurityAnswerHash(passwordEncoder.encode(form.getSecurityAnswer()));

        // Persists the new user to the database.
        return userRepository.save(u);
    }

    // Verifies a raw password against the stored hashed password.
    public boolean verifyPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPasswordHash());
    }

    // Verifies a raw security answer against the stored hashed value.
    public boolean verifySecurityAnswer(User user, String rawAnswer) {
        return passwordEncoder.matches(rawAnswer, user.getSecurityAnswerHash());
    }

    // Updates a user's password by hashing the new value and saving it.
    @Transactional
    public void updatePassword(User user, String newPassword) {
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}