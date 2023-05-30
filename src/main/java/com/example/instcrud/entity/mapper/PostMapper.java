package com.example.instcrud.entity.mapper;

import com.example.instcrud.dto.post.PostDTO;
import com.example.instcrud.entity.Post;
import com.example.instcrud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class PostMapper implements Function<PostDTO, Post> {
    private final UserRepository userRepository;
    @Override
    public Post apply(PostDTO postDTO) {

        var userId = postDTO.postRequestDTO().userId();
        var user = userRepository
                .findById(userId)
                .orElseThrow();

        return Post.builder()
                .id(postDTO.postResponseDTO().id())
                .url(postDTO.postRequestDTO().url())
                .caption(postDTO.postRequestDTO().caption())
                .lat(postDTO.postRequestDTO().lat())
                .lng(postDTO.postRequestDTO().lng())
                .user(user)
                .build();

    }
}
