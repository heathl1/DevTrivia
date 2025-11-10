package com.example.DevTrivia.model;

import com.example.DevTrivia.auth.model.User;
import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
public class Session {
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    User user;
    // Question class - object that contains info for entries in question table
    @Id
    @Column(name ="id")
    private Long id = this.user.getId();
    /*
    @Column(name = "user_id")
    private int user_id;*/

    @Column(name = "score")
    private int score;

    @Column(name = "total_questions")
    private int total_questions;

    @Column(name = "completion_date")
    private Timestamp completion_date;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //getters
    public Long getId() {return this.id;}
    public Long getUserId() {return this.user.getId();}
    public int getScore() {return this.score;}
    public int getTotalQuestions() {return this.total_questions;}
    public java.sql.Timestamp getCompletionDate() {return this.completion_date;}

    // setters
    public void setId(Long id) {this.id = id;}
    public void setUserId(Long user_id) {this.user.setId(user_id);}
    public void setScore(int score) {this.score = score;}
    public void setTotalQuestions(int total_questions) {this.total_questions = total_questions;}
    public void setCompletionDate(java.sql.Timestamp completion_date) {this.completion_date = completion_date;}
}
