package com.example.instcrud.util;

import com.example.instcrud.entity.Comment;
import com.example.instcrud.entity.Post;
import com.example.instcrud.entity.User;
import com.example.instcrud.entity.UserStatus;

import java.util.concurrent.ThreadLocalRandom;

public class TestDataGenerator {
    public static User createRundomUser(String suffix) {
        return User.builder()
                .username("username" + suffix) // e.g. username1
                .email("user" + suffix + "@mail.com") // e.g. user1@mail.com
                .password("Aa12345#") // a valid password
                .status(getRandomUserStatus())
                .build();
    }

    private static UserStatus getRandomUserStatus() {
        return UserStatus.values()[ThreadLocalRandom.current().nextInt(2)];
    }

    public static Post createRandomPost(String suffix){
        var post = new Post();
        post.setUrl("test/url/" + suffix); // e.g. test/url/1
        return post;
    }

    public static Comment createRandomComment(String suffix){
        var comment = new Comment();
        comment.setContents("Testing contents number " + suffix); // e.g. Testing contents number 1
        return comment;
    }
}
