package com.example.devtrivia.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.devtrivia.model.Question;


public interface QuestionRepository extends JpaRepository <Question, Long> {

}
