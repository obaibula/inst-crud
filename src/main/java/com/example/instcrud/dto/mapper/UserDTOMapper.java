package com.example.instcrud.dto.mapper;

import com.example.instcrud.dto.user.UserDTO;
import com.example.instcrud.dto.user.UserRequestDTO;
import com.example.instcrud.dto.user.UserResponseDTO;
import com.example.instcrud.entity.Post;
import com.example.instcrud.entity.User;
import com.example.instcrud.entity.UserStatus;

import java.util.List;
import java.util.function.Function;

public class UserDTOMapper implements Function<User, UserDTO> {
    @Override
    public UserDTO apply(User user) {
        Long userId = user.getId();
        String username = user.getUsername();
        String bio = user.getBio();
        String avatar = user.getAvatar();
        String phone = user.getPhone();
        String email = user.getEmail();
        UserStatus status = user.getStatus();
        List<Post> posts = user.getPosts();

        return new UserDTO(

                new UserResponseDTO(
                        userId,
                        username,
                        bio,
                        avatar,
                        phone,
                        email,
                        status,
                        posts
                ),

                new UserRequestDTO(
                        username,
                        bio,
                        avatar,
                        phone,
                        email,
                        status,
                        posts
                )

        );
    }
}
