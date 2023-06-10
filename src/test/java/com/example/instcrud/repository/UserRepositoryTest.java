package com.example.instcrud.repository;

import com.example.instcrud.entity.Post;
import com.example.instcrud.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class UserRepositoryTest {

    @Container
    public static PostgreSQLContainer container = new PostgreSQLContainer("postgres:14.8")
            .withUsername("postgres")
            .withPassword("12345")
            .withDatabaseName("myinstagram");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    // todo: change the behavior of the population of the db in containers!
    /*@AfterEach
    void tearDown() {
        System.err.println("THE PROBLEM IS HERE******************************");
        userRepository.deleteAll();
        System.err.println("THE PROBLEM IS HERE**************************");
    }*/

    @Test
    void shouldFindAllUsersAndFetchTheirsPostsEagerly() {
        // given

        Long userId = 1L;
        var user = userRepository.findAllFetchPosts(Pageable.unpaged())
                .get()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("User not found with id - " + userId));

        // get the list of ids of all user's posts
        var postIds = user.getPosts()
                .stream()
                .map(Post::getId)
                .toList();

        assertThat(postIds).containsExactlyInAnyOrder(1L, 6L, 7L);

    }

    @Test
    @Transactional
    void shouldDeleteAllUsersAndTheirsOrphansInBulk(){
        Long userId = 4L;
        Long postId = 11L; // post that belongs to the user with id 4;
        Long commentIdByPost = 20L; // comment that belongs to the post with id 11;
        Long commentIdByUser = 7L; // comment that belongs to the user with id 4;

        userRepository.deleteInBulkById(userId);

        var userOptional = userRepository.findById(userId);
        var postOptional = postRepository.findById(postId);
        var postsCommentOptional = commentRepository.findById(commentIdByPost);
        var usersCommentOptional = commentRepository.findById(commentIdByUser);

        assertThat(userOptional.isEmpty()).isTrue();
        assertThat(postOptional.isEmpty()).isTrue();
        assertThat(postsCommentOptional.isEmpty()).isTrue();
        assertThat(usersCommentOptional.isEmpty()).isTrue();

    }

}