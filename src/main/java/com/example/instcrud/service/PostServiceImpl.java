package com.example.instcrud.service;

import com.example.instcrud.dto.PostDTO;
import com.example.instcrud.dto.PostDTOMapper;
import com.example.instcrud.entity.Post;
import com.example.instcrud.exception.PostNotFoundException;
import com.example.instcrud.exception.UserNotFoundException;
import com.example.instcrud.repository.PostRepository;
import com.example.instcrud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostDTOMapper postDTOMapper;
    private final UserRepository userRepository;

    @Override
    public PostDTO findById(long userId,long postId) {
        return postRepository.findByUserIdAndPostId(userId, postId)
                .map(postDTOMapper)
                .orElseThrow(() -> new PostNotFoundException("Not found post with id - " + postId));
    }

    @Override
    @Transactional
    public Post save(Post post, Long userId) {
        var userOptional = userRepository.findById(userId);

        if(userOptional.isPresent()){
            var user = userOptional.get();
            post.setUser(user);
            user.addPost(post);
            return postRepository.save(post);
        }else {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
    }

    @Override
    public List<PostDTO> findAllByUserId(Long userId, Pageable pageable) {
        return postRepository.findAllByUserId(userId, pageable)
                .stream()
                .map(postDTOMapper)
                .collect(Collectors.toList());
    }
}
