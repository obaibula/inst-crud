package com.example.instcrud.controller;

import com.example.instcrud.entity.Comment;
import com.example.instcrud.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.http.ResponseEntity.created;

@RestController
@RequiredArgsConstructor
public class CommentRestController {
    private final CommentService commentService;
    // todo: refactor
    //use the next template to add comment
    // http://localhost:8080/comments?userId=1&postId=1
    @PostMapping("/comments")
    private ResponseEntity<Comment> createComment(@RequestBody Comment comment,
                                                  @RequestParam Long userId,
                                                  @RequestParam Long postId){
        var addedComment = commentService.save(comment, userId, postId);

        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addedComment.getId())
                .toUri();

        return created(location)
                .body(addedComment);
    }
}
