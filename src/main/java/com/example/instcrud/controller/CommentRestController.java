package com.example.instcrud.controller;

import com.example.instcrud.entity.Comment;
import com.example.instcrud.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

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

        var location = getLocation(addedComment);

        return created(location)
                .body(addedComment);
    }

    private URI getLocation(Comment addedComment) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addedComment.getId())
                .toUri();
    }

    @PatchMapping("/comments/{commentId}")
    private ResponseEntity<Void> updateComment(@PathVariable Long commentId,
                                               @RequestBody Map<String, Object> updates){

        commentService.updateComment(commentId, updates);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/comments/{id}")
    private ResponseEntity<Void> deleteComment(@PathVariable Long id){
        commentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
