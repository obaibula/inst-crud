package com.example.instcrud.repository;

import com.example.instcrud.entity.Post;
import com.example.instcrud.entity.User;
import com.example.instcrud.entity.UserStatus;
import com.example.instcrud.exception.UserNotFoundException;
import org.junit.jupiter.api.AfterEach;
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

import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class UserRepositoryTest {

    @Container
    public static PostgreSQLContainer container = new PostgreSQLContainer("postgres:14.8");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @Autowired
    private UserRepository underTest;
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    // todo: change the behavior of the population of the db in containers!
    @AfterEach
    void tearDown() {
        underTest.deleteAllInBatch();
    }

    @Test
    void shouldFindAllUsersAndFetchTheirsPostsEagerly() {
        // given

        Long userId = 1L;
        var user = underTest.findAllFetchPosts(Pageable.unpaged())
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
    void shouldDeleteAllUsersAndTheirsOrphansInBulk() {
        User user = createRandomUser();
        var post = createRandomPost();
        user.addPost(post);
        post.setUser(user);
        underTest.save(user);
        System.err.println(postRepository.findAllByUserId(100l, Pageable.unpaged()));
        Long userId = 4L;
        Long postId = 11L; // post that belongs to the user with id 4;
        Long commentIdByPost = 20L; // comment that belongs to the post with id 11;
        Long commentIdByUser = 7L; // comment that belongs to the user with id 4;

        underTest.deleteInBulkById(userId);

        var userOptional = underTest.findById(userId);
        var postOptional = postRepository.findById(postId);
        var postsCommentOptional = commentRepository.findById(commentIdByPost);
        var usersCommentOptional = commentRepository.findById(commentIdByUser);

        assertThat(userOptional.isEmpty()).isTrue();
        assertThat(postOptional.isEmpty()).isTrue();
        assertThat(postsCommentOptional.isEmpty()).isTrue();
        assertThat(usersCommentOptional.isEmpty()).isTrue();

    }

    private Post createRandomPost() {
        var post = new Post();
        int i = ThreadLocalRandom.current().nextInt(0, 100);

        post.setUrl("http://post.com" + i);
        post.setCaption("the caption" + i);
        post.setLat(ThreadLocalRandom.current().nextFloat(-90, 90));
        post.setLng(ThreadLocalRandom.current().nextFloat(-180, 180));
        return post;
    }

    private User createRandomUser() {
        return User.builder()
                .username(getRandomUsername())
                .bio("test bio")
                .avatar("http://avatar.jpeg")
                .phone(getRandomPhone())
                .email(getRandomEmail())
                .password(getRandomPassword())
                .status(getRandomStatus())
                .build();
    }

    private UserStatus getRandomStatus() {
        if(ThreadLocalRandom.current().nextBoolean())
            return UserStatus.ONLINE;
        else
            return UserStatus.OFFLINE;
    }

    private String getRandomPassword() {
        return "passWORD#" + ThreadLocalRandom.current().nextInt(1, 10000);
    }

    private String getRandomEmail() {
        return "test"
                + ThreadLocalRandom.current().nextInt(1, 99)
                + "@mail.com";

    }

    private String getRandomPhone() {
        return """
                +38 050 133-95-""" + ThreadLocalRandom.current().nextInt(10, 99);
    }

    private String getRandomUsername() {
        return "testuser" + ThreadLocalRandom.current().nextInt(1, 99);
    }

}