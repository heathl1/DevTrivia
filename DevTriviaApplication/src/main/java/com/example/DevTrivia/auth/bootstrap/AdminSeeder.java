package com.example.DevTrivia.auth.bootstrap;

import com.example.DevTrivia.auth.model.User;
import com.example.DevTrivia.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;

@Configuration
public class AdminSeeder {

    @Bean
    public CommandLineRunner seedAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            final String adminUsername = "admin";
            final String adminEmail = "admin@example.com";
            final String adminPasswordPlain = "admin123";

            Optional<User> existing = userRepository.findByUsername(adminUsername);

            if (existing.isEmpty()) {
                User admin = new User();
                admin.setUsername(adminUsername);
                admin.setPasswordHash(passwordEncoder.encode(adminPasswordPlain));
                admin.setEmail(adminEmail);
                admin.setAdmin(true);                   // <-- changed
                admin.setJoinDate(Instant.now());
                admin.setGamesPlayed(0);
                admin.setTotalCorrect(0);
                userRepository.save(admin);
                System.out.println("[AdminSeeder] Created default admin user: " + adminUsername);
            } else {
                User admin = existing.get();
                if (admin.getIsAdmin() == null || !admin.getIsAdmin()) {
                    admin.setAdmin(true);               // <-- changed
                    userRepository.save(admin);
                }
            }
        };
    }
}
