package com.example.DevTrivia.auth.controller;

import com.example.DevTrivia.auth.model.User;
import com.example.DevTrivia.auth.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/details")
    public Map<String, Object> getUserDetails(@AuthenticationPrincipal UserDetails principal) {
        Map<String, Object> response = new HashMap<>();

        if (principal == null) {
            response.put("user", null);
            return response;
        }

        // Reload from DB so gamesPlayed / totalCorrect reflect latest trigger updates
        User user = userService.findByUsername(principal.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Map<String, Object> userDto = new HashMap<>();
        userDto.put("id", user.getId());
        userDto.put("username", user.getUsername());
        userDto.put("gamesPlayed", user.getGamesPlayed());
        userDto.put("totalCorrect", user.getTotalCorrect());
        userDto.put("joinDate", user.getJoinDate());

        response.put("user", userDto);
        return response;
    }
}