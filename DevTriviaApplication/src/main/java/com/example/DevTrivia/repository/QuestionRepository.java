package com.example.DevTrivia.repository; // stores all questions
import com.example.DevTrivia.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    // implementation in controller
}
