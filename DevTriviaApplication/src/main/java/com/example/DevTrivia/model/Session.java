package com.example.DevTrivia.model;

import com.example.DevTrivia.auth.model.User;
import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "session")  // change to your actual table name if different
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "score")
    private int score;

    @Column(name = "total_questions")
    private int total_questions;

    @Column(name = "completion_date")
    private Timestamp completion_date;

    // JPA needs a no-arg constructor
    public Session() {
    }

    // --- getters ---

    public Long getId() {
        return this.id;
    }

    public User getUser() {
        return this.user;
    }

    public int getScore() {
        return this.score;
    }

    public int getTotalQuestions() {
        return this.total_questions;
    }

    public Timestamp getCompletionDate() {
        return this.completion_date;
    }

    // --- setters ---

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setTotalQuestions(int total_questions) {
        this.total_questions = total_questions;
    }

    public void setCompletionDate(Timestamp completion_date) {
        this.completion_date = completion_date;
    }
}