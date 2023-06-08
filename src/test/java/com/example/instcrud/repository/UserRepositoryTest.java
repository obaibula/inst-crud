package com.example.instcrud.repository;

import com.example.instcrud.entity.Post;
import com.example.instcrud.entity.User;
import com.example.instcrud.entity.UserStatus;
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

@Testcontainers
@SpringBootTest
class UserRepositoryTest {

    @Container
    public static PostgreSQLContainer container = new PostgreSQLContainer("postgres:14.8")
            .withUsername("postgres")
            .withPassword("12345")
            .withDatabaseName("myinstagram");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry){
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
        var user = User.builder()
                .username("tested")
                .bio("undertes")
                .avatar("url")
                .phone("+380501339531")
                .email("email@mail.com")
                .password("djsaJoi34")
                .status(UserStatus.ONLINE)
                .build();

        var post = new Post();
        // when
        var users = userRepository.findAllFetchPosts(Pageable.unpaged());
        // then
    }
}