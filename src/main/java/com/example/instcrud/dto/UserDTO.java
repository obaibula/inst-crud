package com.example.instcrud.dto;

import com.example.instcrud.entity.UserStatus;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

public record UserDTO(Long id,
                      String username,
                      String bio,
                      String avatar,
                      String phone,
                      String email,
                      UserStatus status,
                      CollectionModel<EntityModel<PostDTO>> posts) {
}
