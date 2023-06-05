package com.example.instcrud.controller;

import com.example.instcrud.dto.PostDTO;
import com.example.instcrud.entity.Post;
import com.example.instcrud.entity.User;
import com.example.instcrud.repository.UserRepository;
import com.example.instcrud.service.PostService;
import com.example.instcrud.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.*;

@RestController
@RequiredArgsConstructor
public class PostRestController {
    private final PostService postService;

    //use this url as template: "http://localhost:8080/users/3/posts/10" where userId = 3 and postId = 10
    @GetMapping("users/{userId}/posts/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<PostDTO> one(@PathVariable Long userId, @PathVariable Long postId){
        PostDTO post = postService.findById(userId, postId);
        return EntityModel.of(post,
                linkTo(methodOn(PostRestController.class).one(userId, postId)).withSelfRel());
    }

    //use this url as template: "http://localhost:8080/users/3/posts?page=1&size=1"
    @GetMapping("users/{userId}/posts")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<EntityModel<PostDTO>> all(@PathVariable Long userId, Pageable pageable){

        var posts = postService.findAllByUserId(userId, PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "id"))))
                .stream()
                .map(post -> EntityModel.of(post,
                        linkTo(methodOn(PostRestController.class)
                                .one(userId, post.id()))
                                .withSelfRel(),
                        linkTo(methodOn(PostRestController.class)
                                .all(userId, pageable))
                                .withRel("users/" + userId + "/posts")))
                .toList();

        return CollectionModel.of(posts,
                linkTo(methodOn(PostRestController.class).all(userId, pageable)).withSelfRel());
    }

    //todo: refactor
    //use the next template to add post with userId = 3 http://localhost:8080/posts?userId=3
    @PostMapping
    private ResponseEntity<Post> createPost(@RequestBody Post post, @RequestParam Long userId){
        var addedPost = postService.save(post, userId);

        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addedPost.getId())
                .toUri();

        return created(location)
                .body(addedPost);
    }

    //todo: create PATCH, PUT, DELETE methods
}
