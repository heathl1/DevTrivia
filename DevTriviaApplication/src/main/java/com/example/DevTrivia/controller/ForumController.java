package com.example.DevTrivia.controller;

import com.example.DevTrivia.model.ForumPost;
import com.example.DevTrivia.repository.ForumPostRepository;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import com.example.DevTrivia.auth.security.AppUserDetails;

// Controller Marks this class as a Spring MVC controller which handles the requests
@Controller

//end points in this class start with /forum
@RequestMapping("/forum")
public class ForumController {

    //Dependency: our JPA repository that saves and fetches posts from the database
    private final ForumPostRepository forumPostRepository;

    //Spring injects repository automatically
    public ForumController(ForumPostRepository forumPostRepository) {
        this.forumPostRepository = forumPostRepository;
    }

    //GET - gets and shows the forum page & posts
    @GetMapping
    public String getAllPosts(Model model, @AuthenticationPrincipal AppUserDetails userDetails) {
        model.addAttribute("posts",
                forumPostRepository.findAll(
                        org.springframework.data.domain.Sort.by(
                                org.springframework.data.domain.Sort.Direction.DESC,
                                "postTime"
                        )
                )
        );

        model.addAttribute("newPost", new ForumPost());
        return "forum";
    }

    //POST - handles forum input and posts
    @PostMapping
    public String createPost(@ModelAttribute("newPost") ForumPost forumPost,
                             @AuthenticationPrincipal AppUserDetails userDetails) {
        if (forumPost.getPostTime() == null) {
            forumPost.setPostTime(LocalDateTime.now());
        }

        // Set the user who made the post from the authenticated user
        Long currentUserId = userDetails.getUser().getId();
        forumPost.setUserId(currentUserId);

        //Saves post in repoository
        forumPostRepository.save(forumPost);

        //Redirect to prevent resubmisson on page refresh
        return "redirect:/forum";
    }
}