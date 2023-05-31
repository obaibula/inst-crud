package com.example.instcrud.service;

import com.example.instcrud.dto.PostDTO;
import com.example.instcrud.entity.Post;

public interface PostService {
    PostDTO findById(long id);

    Post save(Post post, Long userId);
}
