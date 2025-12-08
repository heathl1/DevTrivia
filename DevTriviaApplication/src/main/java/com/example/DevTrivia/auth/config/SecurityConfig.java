package com.example.DevTrivia.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Use our JPA-backed UserDetailsService with BCrypt
    @Bean
    public DaoAuthenticationProvider authProvider(UserDetailsService uds, PasswordEncoder encoder) {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(uds);
        p.setPasswordEncoder(encoder);
        return p;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // If your public forms don't include CSRF tokens yet, we can ignore CSRF for just these
                .csrf(csrf -> csrf.ignoringRequestMatchers(
                        "/guest",        // POST for "play as guest"
                        "/register",     // POST register
                        "/reset",         // POST password reset
                        "/api/sessions"
                ))
                .authorizeHttpRequests(auth -> auth
                        // Static files
                        .requestMatchers("/", "/login", "/register", "/reset", "/guest",
                                "/css/**", "/js/**").permitAll()
                        // Public pages (GET)
                        .requestMatchers(HttpMethod.GET, "/", "/login", "/register", "/reset").permitAll()
                        // Public form submits (POST)
                        .requestMatchers(HttpMethod.POST, "/guest", "/register", "/reset",  "/api/sessions").permitAll()
                        // Admin area
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // Everything else requires login
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")                  // GET now works
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                );


        return http.build();
    }
}
