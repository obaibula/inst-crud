package com.example.instcrud.service;

import com.example.instcrud.entity.Comment;
import com.example.instcrud.exception.PostNotFoundException;
import com.example.instcrud.exception.UserNotFoundException;
import com.example.instcrud.repository.CommentRepository;
import com.example.instcrud.repository.PostRepository;
import com.example.instcrud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    @Override
    @Transactional
    public Comment save(Comment comment, Long userId, Long postId) {
        var userOptional = userRepository.findById(userId);
        var postOptional = postRepository.findById(postId);

        if(userOptional.isPresent()){
            var user = userOptional.get();
            comment.setUser(user);
            user.addComment(comment);
        }else {
            throw new UserNotFoundException("User not found with id: " + userId);
        }

        if(postOptional.isPresent()){
            var post = postOptional.get();
            comment.setPost(post);
            post.addComment(comment);
        } else {
            throw new PostNotFoundException("Post not found with id: " + postId);
        }

        return commentRepository.save(comment);

    }
}
