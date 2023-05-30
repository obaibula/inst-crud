package com.example.instcrud.service;

import com.example.instcrud.dto.post.PostResponseDTO;
import com.example.instcrud.entity.Post;

public interface PostService {
    PostResponseDTO findById(long id);

    Post save(Post post);
}
