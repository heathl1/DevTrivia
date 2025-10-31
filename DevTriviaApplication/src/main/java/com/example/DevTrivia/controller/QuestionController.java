package com.example.DevTrivia.controller;
import com.example.DevTrivia.model.Question;
import com.example.DevTrivia.repository.QuestionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/questions")
public class QuestionController {
    private final QuestionRepository repository;

    public QuestionController(QuestionRepository repository) {
        this.repository = repository;
    }

    @PostMapping Question createQuestion(/*@Valid*/@RequestBody Question question) {
        return repository.save(question);
    }

    @GetMapping
    public List<Question> getAllQuestions() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Question getQuestionById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Question not found"));
    }

    @PutMapping("/{id}")
    public Question updateQuestion(@PathVariable Long id, /*@Valid*/ @RequestBody Question updatedQuestion) {
        return repository.findById(id)
                .map(question -> {
                    question.setCategoryId(updatedQuestion.getCategoryId());
                    question.setText(updatedQuestion.getText());
                    question.setOptionA(updatedQuestion.getOptionA());
                    question.setOptionB(updatedQuestion.getOptionB());
                    question.setOptionC(updatedQuestion.getOptionC());
                    question.setOptionD(updatedQuestion.getOptionD());
                    question.setCorrectAnswer(updatedQuestion.getCorrectAnswer());
                    return repository.save(question);
                })
                .orElseThrow(() -> new RuntimeException("Question not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteQuestion(@PathVariable Long id) {repository.deleteById(id);}

}
