package com.example.instcrud.repository;

import com.example.instcrud.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByUserIdAndPostId(long userId, long postId);
}
