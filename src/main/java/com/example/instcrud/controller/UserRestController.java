package com.example.instcrud.controller;

import com.example.instcrud.dto.UserDTO;
import com.example.instcrud.entity.User;
import com.example.instcrud.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.created;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;

    //todo: inspect and fix problems gained by OSIV anti-pattern in every method
    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<UserDTO> one(@PathVariable Long userId) {
        UserDTO user = userService.findById(userId);
        return EntityModel.of(user,
                linkTo(methodOn(UserRestController.class)
                        .one(userId))
                        .withSelfRel(),
                linkTo(methodOn(UserRestController.class)
                        .all(Pageable.unpaged()))
                        .withRel("users"));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<EntityModel<UserDTO>> all(Pageable pageable) {

        // read as DTOs, using pagination and adding links (HATEOAS)
        var users = userService.findAll(PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "id"))))
                .stream()
                .map(user -> EntityModel.of(user,
                        linkTo(methodOn(UserRestController.class).one(user.id())).withSelfRel(),
                        linkTo(methodOn(UserRestController.class).all(pageable)).withRel("users")))
                .toList();

        return CollectionModel.of(users, linkTo(methodOn(UserRestController.class).all(pageable)).withSelfRel());
    }

    @PostMapping
    private ResponseEntity<User> createUser(@RequestBody User user) {
        User addedUser = userService.save(user);

        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addedUser.getId())
                .toUri();

        return created(location)
                .body(addedUser);
    }

    @PatchMapping("/{userId}")
    private ResponseEntity<Void> updateUser(@PathVariable Long userId,
                                            @RequestBody Map<String, Object> updates) {
        userService.updateUser(userId, updates);
        return ResponseEntity.noContent().build();
    }

    // todo: too much sql requests? refactor
    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteUser(@PathVariable Long id) {
            userService.deleteById(id);
            return ResponseEntity.noContent().build();
    }

}



















