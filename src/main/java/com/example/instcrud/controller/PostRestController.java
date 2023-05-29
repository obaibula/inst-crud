package com.example.instcrud.controller;

import com.example.instcrud.dto.PostDTO;
import com.example.instcrud.entity.Post;
import com.example.instcrud.repository.UserRepository;
import com.example.instcrud.service.PostService;
import com.example.instcrud.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.created;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostRestController {
    private final PostService postService;
    //temporary
    private final UserRepository userRepository;

    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<PostDTO> one(@PathVariable Long postId){
        PostDTO post = postService.findById(postId);
        return EntityModel.of(post,
                linkTo(methodOn(PostRestController.class).one(postId)).withSelfRel());
    }

    //todo:
    @PostMapping
    private ResponseEntity<Post> createPost(@RequestBody Post post){
        Post addedPost = postService.save(post);

        //temporary(lack of time)
        post.setUser(userRepository.findById(10L).orElseThrow());


        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addedPost.getId())
                .toUri();

        return created(location)
                .body(addedPost);
    }
}
