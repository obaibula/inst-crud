package com.example.instcrud.entity;

import com.example.instcrud.validation.ValidPassword;
import com.example.instcrud.validation.ValidPhone;
import com.example.instcrud.validation.ValidUsername;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
@ToString
@EqualsAndHashCode(of = "id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //will be set to now(), when it is created
    @Column(updatable = false)
    @Setter(AccessLevel.PRIVATE)
    private LocalDateTime createdAt;

    @Setter(AccessLevel.PRIVATE)
    private LocalDateTime updatedAt;

    @Column(nullable = false, unique = true)
    @ValidUsername
    private String username;

    private String bio;

    private String avatar;

    @Column(unique = true)
    @ValidPhone
    private String phone;

    @Column(unique = true)
    @Email //default e-mail validation
    private String email;

    @Column(nullable = false)
    @ValidPassword
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    //todo: get rid of @JsonManagedReference
    @JsonManagedReference
    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Setter(AccessLevel.PRIVATE)
    private List<Post> posts = new ArrayList<>();

    public void addPost(Post post) {
        post.setUser(this);
        posts.add(post);
    }

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Setter(AccessLevel.PRIVATE)
    private List<Comment> comments = new ArrayList<>();

    public void addComment(Comment comment) {
        comment.setUser(this);
        comments.add(comment);
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.setCreatedAt(now);
        this.setUpdatedAt(now);
    }

    @PreUpdate
    protected void onUpdate() {
        this.setUpdatedAt(LocalDateTime.now());
    }

    // The Builder must not include comments and posts fields!
    @Builder
    public User(Long id,
                LocalDateTime createdAt,
                LocalDateTime updatedAt,
                String username,
                String bio,
                String avatar,
                String phone,
                String email,
                String password,
                UserStatus status) {

        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.username = username;
        this.bio = bio;
        this.avatar = avatar;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.status = status;
    }
}
