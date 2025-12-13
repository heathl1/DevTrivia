package com.example.DevTrivia.controller;
import com.example.DevTrivia.auth.model.User;
import com.example.DevTrivia.auth.security.AppUserDetails;
import com.example.DevTrivia.auth.service.UserService;
import com.example.DevTrivia.model.Session;
import com.example.DevTrivia.repository.SessionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

record ScoreData(Integer score, Integer total_questions) {}

@RestController
@RequestMapping("api/sessions")
public class SessionController {
    private final SessionRepository repository;
    private final UserService userService; // handles DB updated

    private record SessionRequest(Integer score, Integer totalQuestions) {}

    // Constructor
    public SessionController(SessionRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> createSession(
            @RequestBody ScoreData data,
            @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        if (userDetails == null) {
            // This is a safety check; Spring Security usually handles this with HTTP 403 Forbidden.
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        User user = userDetails.getUser();
        Session newSession = new Session();

        newSession.setScore(data.score());
        newSession.setTotalQuestions(data.total_questions());

        // Using java.sql.Timestamp, which matches your DDL's 'timestamp' type.
        newSession.setCompletionDate(new Timestamp(System.currentTimeMillis()));

        // CRITICAL: Links the session to the user, satisfying the 'user_id' NOT NULL constraint.
        newSession.setUser(user);

        repository.save(newSession);

        return ResponseEntity.ok().build();
    }


    @GetMapping
    public List<Session> getAllSessions() {return this.repository.findAll();}

    @GetMapping("/{id}")
    public Session getSessionById(@PathVariable long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Session not found"));
    }

    @PutMapping("/{id}")
    public Session updateSession(@PathVariable long id, @RequestBody Session updatedSession) {
        return repository.findById(id)
                .map(session -> {
                    session.setScore(updatedSession.getScore());
                    session.setTotalQuestions(updatedSession.getTotalQuestions());
                    session.setCompletionDate(updatedSession.getCompletionDate());
                    return repository.save(session);
                })
                .orElseThrow(() -> new RuntimeException("Session not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteSession(@PathVariable Long id) {repository.deleteById(id);
    }
}