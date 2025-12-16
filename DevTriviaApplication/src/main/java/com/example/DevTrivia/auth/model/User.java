package com.example.DevTrivia.auth.model;

import jakarta.persistence.*;
import java.time.Instant;

// This entity represents a user account within the DevTrivia application.
@Entity
// Explicitly maps this entity to the "user" table in the database.
@Table(name = "user")
public class User {

    // Primary key for the user table.
    @Id
    // Uses auto-incrementing IDs managed by the database.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Unique username used for login and identification.
    @Column(nullable = false, unique = true, length = 64)
    private String username;

    // Optional email address associated with the user.
    @Column(name = "email", length = 255)
    private String email;

    // Stores the hashed version of the user's password for security.
    @Column(name = "password_hash", length = 255)
    private String passwordHash;

    // Stores the user's chosen security question for account recovery.
    @Column(name = "security_question", length = 255)
    private String securityQuestion;

    // Stores the hashed answer to the security question.
    @Column(name = "security_answer_hash", length = 255)
    private String securityAnswerHash;

    // Timestamp indicating when the user account was created.
    @Column(name = "join_date", nullable = false)
    private Instant joinDate = Instant.now();

    // Tracks how many trivia questions the user has answered correctly.
    @Column(name = "total_correct", nullable = false)
    private Integer totalCorrect = 0;

    // Tracks how many games the user has played.
    @Column(name = "games_played", nullable = false)
    private Integer gamesPlayed = 0;

    // Indicates whether the user has administrative privileges.
    @Column(name = "is_admin", nullable = false)
    private Boolean isAdmin = false;

    // Indicates whether the account is active without needing to delete it.
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    // Default constructor required by JPA.
    public User() {}

    /* ---------- Getters / Setters ---------- */

    // Returns the unique ID for this user.
    public Long getId() { return id; }

    // Sets the unique ID for this user.
    public void setId(Long id) { this.id = id; }

    // Returns the username for this account.
    public String getUsername() { return username; }

    // Sets the username for this account.
    public void setUsername(String username) { this.username = username; }

    // Returns the user's email address.
    public String getEmail() { return email; }

    // Sets the user's email address.
    public void setEmail(String email) { this.email = email; }

    // Returns the hashed password.
    public String getPasswordHash() { return passwordHash; }

    // Sets the hashed password.
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    // Returns the security question.
    public String getSecurityQuestion() { return securityQuestion; }

    // Sets the security question.
    public void setSecurityQuestion(String securityQuestion) { this.securityQuestion = securityQuestion; }

    // Returns the hashed security answer.
    public String getSecurityAnswerHash() { return securityAnswerHash; }

    // Sets the hashed security answer.
    public void setSecurityAnswerHash(String securityAnswerHash) { this.securityAnswerHash = securityAnswerHash; }

    // Returns the date the user joined the application.
    public Instant getJoinDate() { return joinDate; }

    // Updates the join date value.
    public void setJoinDate(Instant joinDate) { this.joinDate = joinDate; }

    // Returns the total number of correct answers.
    public Integer getTotalCorrect() { return totalCorrect; }

    // Updates the total number of correct answers.
    public void setTotalCorrect(Integer totalCorrect) { this.totalCorrect = totalCorrect; }

    // Returns the number of games played by the user.
    public Integer getGamesPlayed() { return gamesPlayed; }

    // Updates the number of games played.
    public void setGamesPlayed(Integer gamesPlayed) { this.gamesPlayed = gamesPlayed; }

    // ----- Admin helpers -----

    // Returns whether the user is marked as an admin.
    public Boolean getIsAdmin() { return isAdmin; }

    // Convenience method to safely check admin status.
    public boolean isAdmin() { return Boolean.TRUE.equals(isAdmin); }

    // Sets the admin flag for this user.
    public void setAdmin(boolean admin) { this.isAdmin = admin; }

    // ----- Enabled helpers -----

    // Returns whether the account is enabled.
    public Boolean getEnabled() {
        return enabled;
    }

    // Enables or disables the user account.
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    // Matches Spring Security's isEnabled convention for account checks.
    public boolean isEnabled() {
        return Boolean.TRUE.equals(enabled);
    }
}