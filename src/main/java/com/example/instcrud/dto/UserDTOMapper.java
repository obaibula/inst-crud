package com.example.instcrud.dto;

import com.example.instcrud.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

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
                user.getPosts()
                        .stream()
                        .map(postDTOMapper)
                        .toList());
    }
}
