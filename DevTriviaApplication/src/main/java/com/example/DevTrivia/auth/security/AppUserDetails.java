package com.example.DevTrivia.auth.security;

import com.example.DevTrivia.auth.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

// Adapts the application's User entity to Spring Security's UserDetails interface.
public class AppUserDetails implements UserDetails {

    // Reference to the underlying User entity from the database.
    private final User user;

    // Wraps a User entity so Spring Security can work with it.
    public AppUserDetails(User user) {
        this.user = user;
    }

    // Determines the user's role based on the admin flag.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        // Checks whether the user has admin privileges.
        boolean admin = Boolean.TRUE.equals(user.getIsAdmin());

        // Assigns the appropriate role used by Spring Security.
        String role = admin ? "ROLE_ADMIN" : "ROLE_USER";

        // Returns the role as a granted authority.
        return List.of(new SimpleGrantedAuthority(role));
    }

    // Returns the hashed password used for authentication.
    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    // Returns the username used during login.
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // Account expiration is not enforced in this application.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Account locking is not enforced in this application.
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Credential expiration is not enforced in this application.
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Uses the enabled flag to allow accounts to be disabled without deletion.
    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(user.isEnabled());
    }

    // Provides access to the underlying User entity when needed.
    public User getUser() {
        return user;
    }
}