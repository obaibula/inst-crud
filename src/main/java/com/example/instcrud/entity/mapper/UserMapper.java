package com.example.instcrud.entity.mapper;

import com.example.instcrud.dto.user.UserDTO;
import com.example.instcrud.entity.User;

import java.util.function.Function;

public class UserMapper implements Function<UserDTO, User> {
    @Override
    public User apply(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.userResponseDTO().id())
                .username(userDTO.userRequestDTO().username())
                .bio(userDTO.userRequestDTO().bio())
                .avatar(userDTO.userRequestDTO().avatar())
                .phone(userDTO.userRequestDTO().phone())
                .email(userDTO.userRequestDTO().email())
                .password("123")
                .status(userDTO.userRequestDTO().status())
                .posts(userDTO.userRequestDTO().posts())
                .build();
    }
}
