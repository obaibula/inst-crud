package com.example.instcrud.service;

import com.example.instcrud.dto.PostDTO;
import com.example.instcrud.entity.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface PostService {
    PostDTO findById(long userId, long postId);

    Post save(Post post, Long userId);

    List<PostDTO> findAllByUserId(Long userId, Pageable pageable);

    void deleteById(Long postId);

    void updatePost(Long postId, Map<String, Object> updates);
}
