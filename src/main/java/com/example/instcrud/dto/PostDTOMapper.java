package com.example.instcrud.dto;

import com.example.instcrud.entity.Post;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class PostDTOMapper implements Function<Post, PostDTO> {
    @Override
    public PostDTO apply(Post post) {
        return new PostDTO(post.getId(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getUrl(),
                post.getCaption(),
                post.getLat(),
                post.getLng(),
                post.getUser().getId());
    }
}
