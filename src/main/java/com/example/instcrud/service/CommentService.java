package com.example.instcrud.service;

import com.example.instcrud.entity.Comment;

public interface CommentService {
    Comment save(Comment comment, Long userId, Long postId);
}
