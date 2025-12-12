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

    // Tracks active sessions so we can expire them later if needed
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    // Keeps SessionRegistry in sync with session lifecycle
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            UserRepository userRepository,
            SessionRegistry sessionRegistry
    ) throws Exception {

        http
                .csrf(csrf -> csrf.ignoringRequestMatchers(
                        "/register",
                        "/reset",
                        "/api/sessions"
                ))

                // If a logged-in user becomes disabled in the DB, kick them out on the next request
                .addFilterBefore(new DisabledUserLogoutFilter(userRepository),
                        UsernamePasswordAuthenticationFilter.class)

                // Session tracking (optional, but keep it since you mentioned "logout everywhere")
                .sessionManagement(session -> session
                        .maximumSessions(-1)
                        .sessionRegistry(sessionRegistry)
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register", "/reset",
                                "/css/**", "/js/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/", "/login", "/register", "/reset").permitAll()
                        .requestMatchers(HttpMethod.POST, "/register", "/reset", "/api/sessions").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)

                        // KEY CHANGE: show disabled message when account is disabled
                        .failureHandler((request, response, exception) -> {
                            if (exception instanceof DisabledException) {
                                response.sendRedirect("/login?disabled");
                            } else {
                                response.sendRedirect("/login?error");
                            }
                        })

                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }
}