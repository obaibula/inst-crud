package com.example.instcrud.dto.user;

import com.example.instcrud.entity.Post;
import com.example.instcrud.entity.UserStatus;

import java.util.List;

public record UserResponseDTO(Long id,
                              String username,
                              String bio,
                              String avatar,
                              String phone,
                              String email,
                              UserStatus status,
                              List<Post> posts) {
}
