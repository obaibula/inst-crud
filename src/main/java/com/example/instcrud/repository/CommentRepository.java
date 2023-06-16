package com.example.instcrud.repository;

import com.example.instcrud.entity.Comment;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByUserIdAndPostId(long userId, long postId);

    //to delete all comments and all cascade-on-delete related entities
    @Modifying
    @Query(value = """
            DELETE
            FROM Comment c
            WHERE c.id = ?1
            """)
    void deleteInBulkById(@NonNull Long commentId);
}
