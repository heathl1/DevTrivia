package com.example.devtrivia.controller;
import com.example.devtrivia.repository.AnswerRepository;
import org.springframework.web.bind.annotation.*;
import com.example.devtrivia.repository.QuestionRepository;
import com.example.devtrivia.model.Question;
//jakarta.validation.constraints.*;
//javax.validation.constraints.*;
import java.util.List;

@RestController
@RequestMapping("/answers")
public class AnswerController {
    private final AnswerRepository repository;

    public AnswerController(AnswerRepository repository) {
        this.repository = repository;
    }
}
