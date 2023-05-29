package com.example.instcrud.service;

import com.example.instcrud.dto.PostDTO;
import com.example.instcrud.dto.PostDTOMapper;
import com.example.instcrud.entity.Post;
import com.example.instcrud.exception.PostNotFoundException;
import com.example.instcrud.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private final PostDTOMapper postDTOMapper;
    @Override
    public PostDTO findById(long id) {
        return postRepository.findById(id)
                .map(postDTOMapper)
                .orElseThrow(() -> new PostNotFoundException("Not found post with id - " + id));
    }

    @Override
    @Transactional
    public Post save(Post post) {
        //todo: check for nulls with appropriate mappings

        return postRepository.save(post);
    }
}
