package com.example.instcrud.service;

import com.example.instcrud.entity.Comment;
import com.example.instcrud.exception.CommentNotFoundException;
import com.example.instcrud.exception.PostNotFoundException;
import com.example.instcrud.exception.UserNotFoundException;
import com.example.instcrud.repository.CommentRepository;
import com.example.instcrud.repository.PostRepository;
import com.example.instcrud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.function.BiConsumer;

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

    @Override
    @Transactional
    public void updateComment(Long commentId, Map<String, Object> updates) {
        var comment = findCommentOrElseThrow(commentId);

        updates.forEach(updateAppropriateFields(comment));

        // no need to check user's and post's presence
        commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteById(Long commentId) {
        findCommentOrElseThrow(commentId);
        commentRepository.deleteInBulkById(commentId);
    }

    private Comment findCommentOrElseThrow(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with commentId - " + commentId));
    }

    // It appears to be useless using the Patch method and such an approach,
    // but it would be helpful when the project grows
    private BiConsumer<String, Object> updateAppropriateFields(Comment comment) {
        return (field, value) -> {
          switch (field){
              case "contents" -> comment.setContents((String) value);
          }
        };
    }
}
