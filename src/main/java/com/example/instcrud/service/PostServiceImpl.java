package com.example.instcrud.service;

import com.example.instcrud.dto.mapper.PostDTOMapper;
import com.example.instcrud.dto.post.PostDTO;
import com.example.instcrud.dto.post.PostRequestDTO;
import com.example.instcrud.dto.post.PostResponseDTO;
import com.example.instcrud.entity.Post;
import com.example.instcrud.entity.mapper.PostMapper;
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
    private final PostMapper postMapper;
    @Override
    public PostResponseDTO findById(long id) {
        return postRepository.findById(id)
                .map(postDTOMapper)
                .map(PostDTO::postResponseDTO)
                .orElseThrow(() -> new PostNotFoundException("Not found post with id - " + id));
    }

    @Override
    @Transactional
    public Post save(PostRequestDTO post) {
        //todo: check for nulls with appropriate mappings
        postMapper.apply();

        return postRepository.save(post);
    }
}
