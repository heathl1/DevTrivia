package com.example.DevTrivia.auth.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "user", indexes = {
        @Index(name = "ux_users_username", columnList = "username", unique = true)
})
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255, unique = true)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(length = 255)
    private String email;


    @Column(name = "security_question", length = 255)
    private String securityQuestion;

    @Column(name = "security_answer_hash", length = 255)
    private String securityAnswerHash;

    @CreationTimestamp 
    @Column(name = "join_date", nullable = false, columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP")
    private Instant joinDate;

    @Column(name = "games_played", nullable = false)
    private Integer gamesPlayed = 0;

    @Column(name = "total_correct", nullable = false)
    private Integer totalCorrect = 0;

    @Column(name = "is_admin", nullable = false)
    private Boolean isAdmin = false;

    // getters/setters…
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; } public void setUsername(String username) { this.username = username; }
    public String getPasswordHash() { return passwordHash; } public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getEmail() { return email; } public void setEmail(String email) { this.email = email; }
    public String getSecurityQuestion() { return securityQuestion; } public void setSecurityQuestion(String securityQuestion) { this.securityQuestion = securityQuestion; }
    public String getSecurityAnswerHash() { return securityAnswerHash; } public void setSecurityAnswerHash(String securityAnswerHash) { this.securityAnswerHash = securityAnswerHash; }
    public Instant getJoinDate() { return joinDate; } public void setJoinDate(Instant joinDate) { this.joinDate = joinDate; }
    public Integer getGamesPlayed() { return gamesPlayed; } public void setGamesPlayed(Integer gamesPlayed) { this.gamesPlayed = gamesPlayed; }
    public Integer getTotalCorrect() { return totalCorrect; } public void setTotalCorrect(Integer totalCorrect) { this.totalCorrect = totalCorrect; }
    public Boolean getIsAdmin() { return isAdmin; } public void setIsAdmin(Boolean admin) { isAdmin = admin; }
}
