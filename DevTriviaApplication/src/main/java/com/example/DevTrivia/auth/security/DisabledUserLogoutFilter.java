package com.example.DevTrivia.auth.security;

import com.example.DevTrivia.auth.model.User;
import com.example.DevTrivia.auth.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class DisabledUserLogoutFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    public DisabledUserLogoutFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Skip this filter for login/register/reset/static so we do not create redirect loops
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        return path.equals("/login")
                || path.equals("/register")
                || path.equals("/reset")
                || path.equals("/guest")
                || path.startsWith("/css/")
                || path.startsWith("/js/");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            String username = auth.getName();

            User u = userRepository.findByUsername(username).orElse(null);

            // If user is missing OR enabled is false -> kick them out (but DO NOT invalidate session)
            if (u == null || !Boolean.TRUE.equals(u.isEnabled())) {

                // Clear Spring Security authentication (logout-from-security only)
                SecurityContextHolder.clearContext();

                // IMPORTANT: we are NOT calling session.invalidate() here
                // This keeps session data intact, but the user becomes unauthenticated

                response.sendRedirect("/login?disabled");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}