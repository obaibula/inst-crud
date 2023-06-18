package com.example.instcrud.repository;

import com.example.instcrud.entity.Post;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static com.example.instcrud.util.TestDataGenerator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @BeforeEach
    void setUp() {
        // create user1 with 2 posts
        var user1 = createRundomUser("1");
        underTest.save(user1);

        var post1 = createRandomPost("1");
        post1.setUser(user1);
        user1.addPost(post1);
        postRepository.save(post1);

        var post2 = createRandomPost("2");
        post2.setUser(user1);
        user1.addPost(post2);
        postRepository.save(post2);

        // create user2 with no posts
        var user2 = createRundomUser("2");
        underTest.save(user2);

        // create comment by user1 on post1
        var comment = createRandomComment("1");
        comment.setPost(post1);
        post1.addComment(comment);
        comment.setUser(user1);
        user1.addComment(comment);
        commentRepository.save(comment);
    }

    @AfterEach
    void tearDown() {
        System.err.println("***************START DELETION***************");
        underTest.deleteAll();
        System.err.println("*************END DELETION*****************");
    }

    @Test
    @Order(1)
    @DisplayName("Should find all users and fetch theirs posts eagerly")
    void shouldFindAllUsersAndFetchTheirsPostsEagerly() {

        // test the method
        var users = underTest.findAllFetchPosts(Pageable.unpaged());

        // verify
        assertThat(users).isNotNull();
        assertThat(users).hasSize(2);

        // get user with appropriate username from db
        List<Post> user1Posts = users.get()
                .filter(user -> user.getUsername().equals("username1"))
                .findAny()
                .orElseThrow()
                .getPosts();
        // user1 has 2 posts
        assertThat(user1Posts).hasSize(2);

        // get user with appropriate username from db
        List<Post> user2Posts = users.get()
                .filter(user -> user.getUsername().equals("username2"))
                .findAny()
                .orElseThrow()
                .getPosts();
        // user2 has no posts
        assertThat(user2Posts).hasSize(0);

    }

    @Test
    @Order(2)
    @DisplayName("""
            Should throw an exception
             when trying to fetch comments
             using findAllFetchPosts method
            """)
    void shouldThrowAnExceptionWhenTryingToFetchComments(){

        // test the method
        var users = underTest.findAllFetchPosts(Pageable.unpaged());

        // verify
        var persistedUser1 = users.get()
                .filter(user -> user.getUsername().equals("username1"))
                .findAny()
                .orElseThrow();

        // as for now there is no Session for comments, see query in the repository
        assertThatThrownBy(() -> persistedUser1.getComments().forEach(System.out::println))
                .isInstanceOf(LazyInitializationException.class);
    }

    // todo: it must be redone!!!!
    @Test
    @Order(3)
    @DisplayName("""
            Should delete all users
             and theirs orphans in bulk
            """)
    @Transactional
    void shouldDeleteAllUsersAndTheirsOrphansInBulk() {
        // fetch user with "username1"
        var user1 = underTest.findAll()
                        .stream()
                .filter(user -> user.getUsername().equals("username1"))
                .findAny()
                .orElseThrow();

        Long userId = user1.getId();
        Long postId = user1.getPosts().get(0).getId();
        Long commentId = user1.getComments().get(0).getId();

        underTest.deleteInBulkById(userId);

        var userOptional = underTest.findById(userId);
        var postOptional = postRepository.findById(postId);
        var commentOptional = commentRepository.findById(commentId);


        assertThat(userOptional).isEmpty();
        assertThat(postOptional).isEmpty();
        assertThat(commentOptional).isEmpty();
    }

}