package com.example.instcrud.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String username;
    private String bio;
    private String avatar;
    private String phone;
    private String email;
    private String password;
    private UserStatus status;
}
