package com.example.DevTrivia.auth.repository;

import com.example.DevTrivia.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Repository interface for performing database operations on User entities.
public interface UserRepository extends JpaRepository<User, Long> {

    // Retrieves a user by username, commonly used during login and authentication.
    Optional<User> findByUsername(String username);

    // Checks if a username already exists to prevent duplicate accounts.
    boolean existsByUsername(String username);

    // Checks if an email address is already associated with an account.
    boolean existsByEmail(String email);
}
