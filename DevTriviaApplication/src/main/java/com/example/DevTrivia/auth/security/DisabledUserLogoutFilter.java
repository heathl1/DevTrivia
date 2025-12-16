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

// Custom security filter that logs out users who become disabled while logged in.
public class DisabledUserLogoutFilter extends OncePerRequestFilter {

    // Repository used to re-check user status on each request.
    private final UserRepository userRepository;

    // Injects the UserRepository so we can verify the user's enabled state.
    public DisabledUserLogoutFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Skips this filter for public routes to avoid redirect loops.
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        // Extracts the request path for comparison.
        String path = request.getServletPath();

        // Public pages and static resources do not need this check.
        return path.equals("/login")
                || path.equals("/register")
                || path.equals("/reset")
                || path.equals("/guest")
                || path.startsWith("/css/")
                || path.startsWith("/js/");
    }

    // Executes once per request to verify the user's enabled status.
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Retrieves the current authentication from the security context.
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Continues only if a real user is authenticated.
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {

            // Gets the username of the currently logged-in user.
            String username = auth.getName();

            // Re-fetches the user from the database to ensure current status.
            User u = userRepository.findByUsername(username).orElse(null);

            // If the user no longer exists or has been disabled, force logout.
            if (u == null || !Boolean.TRUE.equals(u.isEnabled())) {

                // Clears the Spring Security authentication context.
                SecurityContextHolder.clearContext();

                // Intentionally does NOT invalidate the HTTP session.
                // This avoids unexpected side effects and keeps session data intact.

                // Redirects the user to login with a disabled message.
                response.sendRedirect("/login?disabled");
                return;
            }
        }

        // Continues the filter chain if the user is still valid.
        filterChain.doFilter(request, response);
    }
}