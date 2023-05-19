package com.example.instcrud;

import com.example.instcrud.entity.User;
import com.example.instcrud.entity.UserStatus;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserJsonTest {
    @Autowired
    private JacksonTester<User> json;
    @Autowired
    private JacksonTester<User[]> jsonList;
    private User[] users;

    @BeforeEach
    void setUp() {
        users = Arrays.array(
                User.builder()
                        .id(66L)
                        .createdAt(LocalDateTime.parse("2023-05-19T12:44:28"))
                        .updatedAt(LocalDateTime.parse("2023-05-19T12:59:28"))
                        .username("oleh123")
                        .phone("+380501339531")
                        .status(UserStatus.OFFLINE)
                        .build()
        );
    }

    @Test
    public void userSerializationTest() throws IOException {
        User user = users[0];

        System.out.println(json.write(user));

        assertThat(json.write(user)).isStrictlyEqualToJson("singleUser.json");

        assertThat(json.write(user)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(user)).extractingJsonPathNumberValue("@.id")
                .isEqualTo(66);

        assertThat(json.write(user)).hasJsonPathStringValue("@.username");
        assertThat(json.write(user)).extractingJsonPathStringValue("@.username")
                .isEqualTo("oleh123");


        assertThat(json.write(user)).hasJsonPathStringValue("@.createdAt");
        assertThat(json.write(user)).extractingJsonPathStringValue("@.createdAt")
                .isEqualTo("2023-05-19T12:44:28");
    }

    @Test
    public void userDeserializationTest() throws IOException {
        String expected = """
                {
                  "id": 66,
                  "createdAt": "2023-05-19T12:44:28",
                  "updatedAt": "2023-05-19T12:59:28",
                  "username": "oleh123",
                  "bio": null,
                  "avatar": null,
                  "phone": "+380501339531",
                  "email": null,
                  "password": null,
                  "status": "OFFLINE"
                }
                """;

        assertThat(json.parse(expected))
                .isEqualTo(users[0]);
        assertThat(json.parseObject(expected).getId())
                .isEqualTo(66);
        assertThat(json.parseObject(expected).getStatus())
                .isEqualTo(UserStatus.OFFLINE);
    }
}





















