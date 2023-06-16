package com.example.instcrud.controller;

import com.example.instcrud.dto.PostDTO;
import com.example.instcrud.entity.Post;
import com.example.instcrud.service.PostService;
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

import java.net.URI;
import java.util.Map;
import java.util.function.Function;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.created;

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
                getLinkToOneMethod(userId, postId).withSelfRel());
    }

    //use this url as template: "http://localhost:8080/users/3/posts?page=1&size=1"
    @GetMapping("users/{userId}/posts")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<EntityModel<PostDTO>> all(@PathVariable Long userId, Pageable pageable){

        var posts = postService.findAllByUserId(userId, createPageRequest(pageable))
                .stream()
                .map(getPostDTOEntityModelFunction(userId, pageable))
                .toList();

        return CollectionModel.of(posts,
                getLinkToAllMethod(userId, pageable).withSelfRel());
    }

    private Function<PostDTO, EntityModel<PostDTO>> getPostDTOEntityModelFunction(Long userId, Pageable pageable) {
        return postDTO -> getDtoEntityModel(userId, pageable, postDTO);
    }

    private EntityModel<PostDTO> getDtoEntityModel(Long userId, Pageable pageable, PostDTO postDTO) {
        return EntityModel.of(postDTO,
                getLinkToOneMethod(userId, postDTO.id())
                        .withSelfRel(),
                getLinkToAllMethod(userId, pageable)
                        .withRel("users/" + userId + "/posts"));
    }

    private WebMvcLinkBuilder getLinkToAllMethod(Long userId, Pageable pageable) {
        return linkTo(methodOn(PostRestController.class)
                .all(userId, pageable));
    }

    private WebMvcLinkBuilder getLinkToOneMethod(Long userId, Long postId) {
        return linkTo(methodOn(PostRestController.class)
                .one(userId, postId));
    }

    private PageRequest createPageRequest(Pageable pageable) {
        return PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "id")));
    }

    // todo: refactor
    //use the next template to add post with userId = 3
    // http://localhost:8080/posts?userId=3
    @PostMapping("/posts")
    private ResponseEntity<Post> createPost(@RequestBody Post post, @RequestParam Long userId){
        var addedPost = postService.save(post, userId);

        var location = getLocation(addedPost);

        return created(location)
                .body(addedPost);
    }

    private URI getLocation(Post addedPost) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addedPost.getId())
                .toUri();
    }

    @DeleteMapping("posts/{id}")
    private ResponseEntity<Void> deletePost(@PathVariable Long id){
        postService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("posts/{postId}")
    private ResponseEntity<Void> updatePost(@PathVariable Long postId,
                                            @RequestBody Map<String, Object> updates){
        postService.updatePost(postId, updates);
        return ResponseEntity.noContent().build();
    }
}
