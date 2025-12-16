package com.example.DevTrivia.auth.security;

import com.example.DevTrivia.auth.model.User;
import com.example.DevTrivia.auth.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Integrates the application's User model with Spring Security authentication.
@Service
public class JpaUserDetailsService implements UserDetailsService {

    // Repository used to look up users during authentication.
    private final UserRepository userRepository;

    // Injects the user repository for database access.
    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Loads a user by username when Spring Security attempts authentication.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Attempts to find the user in the database or fails authentication if not found.
        User u = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Wraps the User entity in a UserDetails implementation for Spring Security.
        return new AppUserDetails(u);
    }
}