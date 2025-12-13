package com.example.DevTrivia.repository;

import com.example.DevTrivia.model.ForumPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

//Spring JPA will auto implement the interfaces for database access
public interface ForumPostRepository extends JpaRepository<ForumPost, Long> {
}