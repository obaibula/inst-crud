package com.example.instcrud.repository;

import com.example.instcrud.dto.PostDTOMapper;
import com.example.instcrud.entity.Post;
import com.example.instcrud.entity.User;
import com.example.instcrud.entity.UserStatus;
import com.example.instcrud.exception.UserNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

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

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void shouldFindAllUsersAndFetchTheirsPostsEagerly() {
        // given

        // todo: change the behavior of the population of the db in containers!
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
}