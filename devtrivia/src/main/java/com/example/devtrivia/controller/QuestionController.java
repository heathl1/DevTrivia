package com.example.devtrivia.controller;
import org.springframework.web.bind.annotation.*;
import com.example.devtrivia.repository.QuestionRepository;
import com.example.devtrivia.model.Question;
//jakarta.validation.constraints.*;
//javax.validation.constraints.*;
import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionController {
    private final QuestionRepository repository;

    public QuestionController(QuestionRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Question createQuestion(/*@Valid*/ @RequestBody Question question) {
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
                    question.setQuestionText(updatedQuestion.getQuestionText());
                    question.setDifficultyLevel(updatedQuestion.getDifficultyLevel());
                    question.setCategoryId(updatedQuestion.getCategoryId());
                    return repository.save(question);
                })
                .orElseThrow(() -> new RuntimeException("Question not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteQuestion(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
