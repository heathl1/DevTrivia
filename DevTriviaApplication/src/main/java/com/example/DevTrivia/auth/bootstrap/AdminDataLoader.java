package com.example.DevTrivia.auth.bootstrap;

import com.example.DevTrivia.auth.model.User;
import com.example.DevTrivia.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;

@Component
public class AdminDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminDataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        userRepository.findByUsername("admin").ifPresentOrElse(
                u -> { /* already present */ },
                () -> {
                    User admin = new User();
                    admin.setUsername("admin");
                    admin.setEmail("admin@example.com");
                    admin.setPasswordHash(passwordEncoder.encode("admin123"));
                    admin.setAdmin(true);                 // <-- use setAdmin
                    admin.setJoinDate(Instant.now());
                    admin.setGamesPlayed(0);
                    admin.setTotalCorrect(0);
                    userRepository.save(admin);
                    System.out.println("[AdminDataLoader] Seeded admin user");
                }
        );
    }
}
