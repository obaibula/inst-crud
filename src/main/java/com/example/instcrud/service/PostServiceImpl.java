package com.example.instcrud.service;

import com.example.instcrud.dto.PostDTO;
import com.example.instcrud.dto.PostDTOMapper;
import com.example.instcrud.entity.Post;
import com.example.instcrud.exception.PostNotFoundException;
import com.example.instcrud.exception.UserNotFoundException;
import com.example.instcrud.repository.PostRepository;
import com.example.instcrud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostDTOMapper postDTOMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public PostDTO findById(long userId, long postId) {
        return postRepository.findByUserIdAndPostId(userId, postId)
                .map(postDTOMapper)
                .orElseThrow(() -> checkWhetherUserExistsAndGetAppropriateException(userId, postId));
    }

    private RuntimeException checkWhetherUserExistsAndGetAppropriateException(long userId, long postId) {
            if (userRepository.existsById(userId)) {
                return new PostNotFoundException("Not found post with id - " + postId);
            } else return new UserNotFoundException("User not found with id: " + userId);
    }

    @Override
    @Transactional
    public Post save(Post post, Long userId) {
        var userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            var user = userOptional.get();
            post.setUser(user);
            user.addPost(post);
            return postRepository.save(post);
        } else {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostDTO> findAllByUserId(Long userId, Pageable pageable) {
        Page<Post> allByUserId = postRepository.findAllByUserId(userId, pageable);

        if (allByUserId.isEmpty()) {
            if (userRepository.existsById(userId)) {
                throw new PostNotFoundException("user does not have any posts with userId - " + userId);
            } else {
                throw new UserNotFoundException("user is not found with id - " + userId);
            }
        }

        return allByUserId
                .map(postDTOMapper)
                .toList();
    }

    @Override
    @Transactional
    public void deleteById(Long postId) {
        findPostOrElseThrow(postId);
        postRepository.deleteInBulkById(postId);
    }

    @Override
    @Transactional
    public void updatePost(Long postId, Map<String, Object> updates) {
        var post = findPostOrElseThrow(postId);

        updates.forEach(updateAppropriateFields(post));

        // If the post has been found successfully,
        // there is no need to check if the user exists,
        // like we did in the save method in this class
        postRepository.save(post);
    }

    private BiConsumer<String, Object> updateAppropriateFields(Post post) {
        return (field, value) -> {
            switch (field){
                case "url" -> post.setUrl((String) value);
                case "caption" -> post.setCaption((String) value);
                // It appears that the number from JSON is parsed as a Double,
                // so it cannot be implicitly cast
                case "lat" -> post.setLat(((Double) value).floatValue());
                case "lng" -> post.setLng(((Double) value).floatValue());
            }
        };
    }

    private Post findPostOrElseThrow(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Not found post with postId - " + postId));
    }
}
