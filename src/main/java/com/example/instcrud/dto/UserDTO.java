package com.example.instcrud.dto;

import com.example.instcrud.entity.Post;
import com.example.instcrud.entity.UserStatus;

import java.util.List;

public record UserDTO(Long id,
                      String username,
                      String bio,
                      String avatar,
                      String phone,
                      String email,
                      UserStatus status,
                      List<PostDTO> posts) {
}
