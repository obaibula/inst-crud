package com.example.instcrud.controller;

import com.example.instcrud.dto.UserDTO;
import com.example.instcrud.entity.User;
import com.example.instcrud.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.created;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<UserDTO> one(@PathVariable Long userId){
        UserDTO user = userService.findById(userId);
        return EntityModel.of(user,
                linkTo(methodOn(UserRestController.class).one(userId)).withSelfRel());
    }

    @PostMapping
    private ResponseEntity<User> createUser(@RequestBody User user){
        User addedUser = userService.save(user);

        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addedUser.getId())
                .toUri();

        return created(location)
                .body(addedUser);
    }
}
