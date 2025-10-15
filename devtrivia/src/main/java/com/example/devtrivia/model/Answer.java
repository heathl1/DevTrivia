package com.example.devtrivia.model;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

/*
answer_id
question_id
answer_text
is_correct
display_order
 */

@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private int answer_id;

    @Column(name = "question_id")
    private int question_id;

    @Column(name = "answer_text")
    private String answer_text;

    @Column(name = "is_correct")
    private boolean is_correct;

    @Column(name = "display_order")
    private int display_order;

    // getters
    public int getAnswerId() {
        return this.answer_id;
    }

    public int getQuestionId() {
        return this.question_id;
    }

    public String getQuestionText() {
        return this.answer_text;
    }

    public boolean getIsCorrect() {
        return this.is_correct;
    }

    public int getDisplayOrder() {
        return this.display_order;
    }

    //setters

}
