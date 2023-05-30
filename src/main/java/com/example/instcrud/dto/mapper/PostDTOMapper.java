package com.example.instcrud.dto.mapper;

import com.example.instcrud.dto.post.PostDTO;
import com.example.instcrud.dto.post.PostRequestDTO;
import com.example.instcrud.dto.post.PostResponseDTO;
import com.example.instcrud.entity.Post;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class PostDTOMapper implements Function<Post, PostDTO> {
    @Override
    public PostDTO apply(Post post) {

        String url = post.getUrl();
        String caption = post.getCaption();
        Float lat = post.getLat();
        Float lng = post.getLng();
        Long userId = post.getUser().getId();
        Long postId = post.getId();

        return new PostDTO(

                new PostResponseDTO(
                        postId,
                        url,
                        caption,
                        lat,
                        lng,
                        userId
                ),
                new PostRequestDTO(
                        url,
                        caption,
                        lat,
                        lng,
                        userId
                )

        );
    }
}
