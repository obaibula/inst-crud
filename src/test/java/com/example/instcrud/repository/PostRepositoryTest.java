package com.example.instcrud.repository;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.example.instcrud.util.TestDataGenerator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@Testcontainers
@SpringBootTest(properties = "spring.flyway.clean-disabled=false")
class PostRepositoryTest {

    @Container
    public static PostgreSQLContainer container = new PostgreSQLContainer("postgres:14.8");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository underTest;
    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp(@Autowired Flyway flyway) {
        // clear the db and migrate
        flyway.clean();
        flyway.migrate();

        // create user1 with 2 posts
        var user1 = createRundomUser("1");
        userRepository.save(user1);

        var post1 = createRandomPost("1");
        post1.setUser(user1);
        user1.addPost(post1);
        underTest.save(post1);

        var post2 = createRandomPost("2");
        post2.setUser(user1);
        user1.addPost(post2);
        underTest.save(post2);

        // create user2 with no posts
        var user2 = createRundomUser("2");
        userRepository.save(user2);

        // create comment by user1 on post1
        var comment = createRandomComment("1");
        comment.setPost(post1);
        post1.addComment(comment);
        comment.setUser(user1);
        user1.addComment(comment);
        commentRepository.save(comment);
    }

    @Test
    @Order(1)
    void shouldFindAPostIfUserExistsByUserIdAndPostId() {
        // given
        var user1 = userRepository.findAllFetchPosts(Pageable.unpaged())
                .stream()
                .filter(user -> user.getUsername().equals("username1"))
                .findAny()
                .orElseThrow();
        Long userId = user1.getId();
        Long postId = user1.getPosts().get(0).getId();
        // when
        var foundPost = underTest.findByUserIdAndPostId(userId, postId)
                .orElseThrow();

        // then
        assertThat(foundPost.getUrl()).isEqualTo("test/url/1");
    }

}