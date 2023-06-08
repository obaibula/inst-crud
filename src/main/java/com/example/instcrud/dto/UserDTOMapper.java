package com.example.instcrud.dto;

import com.example.instcrud.controller.PostRestController;
import com.example.instcrud.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.function.Function;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class UserDTOMapper implements Function<User, UserDTO> {
    private final PostDTOMapper postDTOMapper;
    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getBio(),
                user.getAvatar(),
                user.getPhone(),
                user.getEmail(),
                user.getStatus(),
                getCollectionModelOfPosts(user));
    }

    //to fetch not only PostDTO's, but with links!
    private CollectionModel<EntityModel<PostDTO>> getCollectionModelOfPosts(User user) {
        var userId = user.getId();
        var posts = user.getPosts()
                .stream()
                .map(postDTOMapper)
                .map(postDTO -> EntityModel.of(postDTO,
                        linkTo(methodOn(PostRestController.class)
                                .one(userId, postDTO.id()))
                                .withSelfRel(),
                        linkTo(methodOn(PostRestController.class)
                                .all(userId, Pageable.unpaged()))
                                .withRel("users/" + userId + "/posts")))
                .toList();

        return CollectionModel.of(posts,
                linkTo(methodOn(PostRestController.class).all(userId, Pageable.unpaged())).withSelfRel());
    }
}
