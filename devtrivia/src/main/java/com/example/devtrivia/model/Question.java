package com.example.devtrivia.model;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

/*
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
 */
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private int question_id;

    @NotNull
    @Column(name = "question_text")
    private String question_text;

    public enum level { Easy, Medium, Hard}
    @NotNull
    @Column(name = "difficulty_level")
    private level difficulty_level;

    @NotNull
    @Column(name = "category_id")
    private int category_id;

    // getters and setters

    public int getQuestionId() {
        return question_id;
    }

    public void setQuestionId(int questionId) {
        this.question_id = questionId;
    }

    public String getQuestionText() {
        return question_text;
    }

    public void setQuestionText(String text) {
        this.question_text = question_text;
    }

    public level getDifficultyLevel() {
        return this.difficulty_level;
    }

    public void setDifficultyLevel(level difficulty_level) {
        this.difficulty_level = difficulty_level;
    }

    public int getCategoryId() {
        return this.category_id;
    }

    public void setCategoryId(int category_id) {
        this.category_id = category_id;
    }

}
