package com.example.instcrud.service;

import com.example.instcrud.entity.Comment;

import java.util.Map;

public interface CommentService {
    Comment save(Comment comment, Long userId, Long postId);

    void updateComment(Long commentId, Map<String, Object> updates);

    void deleteById(Long commentId);
}
