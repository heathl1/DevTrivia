package com.example.DevTrivia.auth.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    private String username;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "password_hash", length = 255)
    private String passwordHash;

    @Column(name = "security_question", length = 255)
    private String securityQuestion;

    @Column(name = "security_answer_hash", length = 255)
    private String securityAnswerHash;

    @Column(name = "join_date", nullable = false)
    private Instant joinDate = Instant.now();

    @Column(name = "total_correct", nullable = false)
    private Integer totalCorrect = 0;

    @Column(name = "games_played", nullable = false)
    private Integer gamesPlayed = 0;

    // Existing admin flag
    @Column(name = "is_admin", nullable = false)
    private Boolean isAdmin = false;

    // NEW: enabled flag so we can disable accounts instead of deleting them
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    public User() {}

    /* ---------- Getters / Setters ---------- */

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getSecurityQuestion() { return securityQuestion; }
    public void setSecurityQuestion(String securityQuestion) { this.securityQuestion = securityQuestion; }

    public String getSecurityAnswerHash() { return securityAnswerHash; }
    public void setSecurityAnswerHash(String securityAnswerHash) { this.securityAnswerHash = securityAnswerHash; }

    public Instant getJoinDate() { return joinDate; }
    public void setJoinDate(Instant joinDate) { this.joinDate = joinDate; }

    public Integer getTotalCorrect() { return totalCorrect; }
    public void setTotalCorrect(Integer totalCorrect) { this.totalCorrect = totalCorrect; }

    public Integer getGamesPlayed() { return gamesPlayed; }
    public void setGamesPlayed(Integer gamesPlayed) { this.gamesPlayed = gamesPlayed; }

    // ----- Admin helpers -----
    public Boolean getIsAdmin() { return isAdmin; }

    public boolean isAdmin() { return Boolean.TRUE.equals(isAdmin); }

    public void setAdmin(boolean admin) { this.isAdmin = admin; }

    // ----- Enabled helpers -----
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    // This style works well with Spring Security (UserDetails.isEnabled)
    public boolean isEnabled() {
        return Boolean.TRUE.equals(enabled);
    }
}
