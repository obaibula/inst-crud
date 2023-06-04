package com.example.instcrud.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void shouldFindAllUsersAndFetchTheirsPostsEagerly() {
        // given
        // when
        // then
    }
}