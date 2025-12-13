package com.example.DevTrivia.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

//JPA Entity mapping to the table forum_posts
@Entity
@Table(name = "forum_posts")

public class ForumPost {

    @Id

    //generates ID incrementally in SQL
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //stores ID in user_id column in table
    @Column(name = "user_id")
    private Long userId;

    //allows text to be longer than VARCHAR
    @Column(columnDefinition = "TEXT")
    private String content;

    //stores post date stamp
    @Column(name = "post_time")
    private LocalDateTime postTime;

    //Getters + Setters... self explanatory
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
}

