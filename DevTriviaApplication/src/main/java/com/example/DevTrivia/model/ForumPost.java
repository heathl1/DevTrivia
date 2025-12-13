package com.example.DevTrivia.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

//JPA Entity mapping to the table forum_posts
@Entity
@Table(name = "forum_post")
public class ForumPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000, nullable = false)
    private String content;

    @Column(name = "post_time", nullable = false)
    private LocalDateTime postTime;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "username", length = 64, nullable = false)
    private String username;

    @PrePersist
    protected void onCreate() {
        if (postTime == null) {
            postTime = LocalDateTime.now();
        }
        if (username == null || username.isBlank()) {
            username = "User#" + userId;
        }
    }
    //Getters + Setters... self-explanatory
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getPostTime() {
        return postTime;
    }

    public void setPostTime(LocalDateTime postTime) {
        this.postTime = postTime;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }
}
