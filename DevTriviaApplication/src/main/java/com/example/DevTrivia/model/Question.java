package com.example.DevTrivia.model;

import jakarta.persistence.*;


@Entity
public class Question {
    // Question class - object that contains info for entries in question table
    @Id
    @Column(name ="id")
    //@GeneratedValue(strategy = GenerationType.IDENTITY) ;  //unsure if needed because id is automated
    private int id;

    @Column(name = "category_id")
    private int category_id;

    @Column(name = "text")
    private String text;

    @Column(name = "option_a")
    private String option_a;

    @Column(name = "option_b")
    private String option_b;

    @Column(name = "option_c")
    private String option_c;

    @Column(name = "option_d")
    private String option_d;

    @Column(name = "correct_answer")
    private String correct_answer;

    // Getters
    public int getId(){
        return this.id;
    }

    public int getCategoryId() {
        return this.category_id;
    }

    public String getText() {
        return this.text;
    }

    public String getOptionA() {
        return this.option_a;
    }

    public String getOptionB() {
        return this.option_b;
    }

    public String getOptionC() {
        return this.option_c;
    }

    public String getOptionD() {
        return this.option_d;
    }

    public String getCorrectAnswer() {
        return this.correct_answer;
    }

    // setters


    public void setId(int id) {  //unsure if needed because id is automated
        this.id = id;
    }

    public void setCategoryId(int category_id) {
        this.category_id = category_id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setOptionA(String option_a) {
        this.option_a = option_a;
    }

    public void setOptionB(String option_b) {
        this.option_b = option_b;
    }

    public void setOptionC(String option_c) {
        this.option_c = option_c;
    }

    public void setOptionD(String option_d) {
        this.option_d = option_d;
    }

    public void setCorrectAnswer(String correct_answer) {
        this.correct_answer = correct_answer;
    }

}
