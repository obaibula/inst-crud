package com.example.instcrud.dto;

import com.example.instcrud.entity.UserStatus;

public record UserDTO(Long id,
                      String username,
                      String bio,
                      String avatar,
                      String phone,
                      String email,
                      UserStatus status) {
}
