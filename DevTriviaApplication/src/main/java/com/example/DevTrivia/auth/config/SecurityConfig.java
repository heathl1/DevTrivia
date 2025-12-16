package com.example.DevTrivia.auth.config;

import com.example.DevTrivia.auth.repository.UserRepository;
import com.example.DevTrivia.auth.security.DisabledUserLogoutFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

// Central configuration for Spring Security in the DevTrivia application.
@Configuration
// Enables method-level security annotations if we choose to use them (ex: @PreAuthorize).
@EnableMethodSecurity
public class SecurityConfig {

    // Uses BCrypt to hash passwords and security answers consistently across the app.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Connects our JPA-backed UserDetailsService to Spring Security authentication.
    @Bean
    public DaoAuthenticationProvider authProvider(UserDetailsService uds, PasswordEncoder encoder) {

        // DaoAuthenticationProvider compares login credentials using our user lookup + encoder.
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(uds);
        p.setPasswordEncoder(encoder);
        return p;
    }

    // Tracks active sessions so we can manage or expire sessions later if needed.
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    // Keeps SessionRegistry accurate as sessions are created and destroyed.
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    // Defines the main security filter chain: CSRF, filters, route rules, login, and logout.
    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            UserRepository userRepository,
            SessionRegistry sessionRegistry
    ) throws Exception {

        http
                // Disables CSRF checks for specific endpoints that are used by forms/API calls.
                // This avoids false CSRF failures while keeping CSRF protection for everything else.
                .csrf(csrf -> csrf.ignoringRequestMatchers(
                        "/register",
                        "/reset",
                        "/api/sessions"
                ))

                // If a logged-in user gets disabled in the database, force logout on the next request.
                .addFilterBefore(new DisabledUserLogoutFilter(userRepository),
                        UsernamePasswordAuthenticationFilter.class)

                // Configures session tracking and limits.
                // -1 means unlimited concurrent sessions (useful during development/testing).
                .sessionManagement(session -> session
                        .maximumSessions(-1)
                        .sessionRegistry(sessionRegistry)
                )

                // Defines which requests are public vs protected.
                .authorizeHttpRequests(auth -> auth

                        // Public pages and static assets.
                        .requestMatchers("/", "/login", "/register", "/reset",
                                "/css/**", "/js/**").permitAll()

                        // Explicitly allow GET access to the main public pages.
                        .requestMatchers(HttpMethod.GET, "/", "/login", "/register", "/reset").permitAll()

                        // Allow POST actions for registration, password reset, and session-related calls.
                        .requestMatchers(HttpMethod.POST, "/register", "/reset", "/api/sessions").permitAll()

                        // Admin routes require the ADMIN role.
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Everything else requires a logged-in user.
                        .anyRequest().authenticated()
                )

                // Configures form-based login using our custom login page.
                .formLogin(form -> form
                        .loginPage("/login")

                        // Sends users to the homepage after successful login.
                        .defaultSuccessUrl("/", true)

                        // Redirects with a clearer message when an account is disabled.
                        .failureHandler((request, response, exception) -> {
                            if (exception instanceof DisabledException) {
                                response.sendRedirect("/login?disabled");
                            } else {
                                response.sendRedirect("/login?error");
                            }
                        })

                        // Allows everyone to access the login endpoint itself.
                        .permitAll()
                )

                // Configures logout behavior and session cleanup.
                .logout(logout -> logout
                        .logoutUrl("/logout")

                        // After logout, send users back to login with a logout message.
                        .logoutSuccessUrl("/login?logout")

                        // Fully clears the session and authentication state.
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)

                        // Removes the session cookie so the browser cannot reuse it.
                        .deleteCookies("JSESSIONID")
                );

        // Builds and returns the configured filter chain.
        return http.build();
    }
}