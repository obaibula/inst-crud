package com.example.instcrud.repository;

import com.example.instcrud.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
            SELECT p
            FROM User u
            JOIN u.posts p
            WHERE p.user.id = :userId AND p.id = :postId
            """)
    Optional<Post> findByUserIdAndPostId(long userId, long postId);

    Page<Post> findAllByUserId(Long userId, Pageable pageable);
}
