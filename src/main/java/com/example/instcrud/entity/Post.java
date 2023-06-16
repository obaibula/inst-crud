package com.example.instcrud.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Setter @Getter
@NoArgsConstructor
//todo: create appropriate exception handler for this constraint
@Check(constraints = """
        lat IS NULL OR (lng IS NOT NULL AND lat >= -90 AND lat <= 90)
        AND
        lng IS NULL OR (lat IS NOT NULL AND lng >= -180 AND lng <= 180)
        """)
@ToString(exclude = "user")
@EqualsAndHashCode(of = "id")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    @Setter(AccessLevel.PRIVATE)
    private LocalDateTime createdAt;

    @Setter(AccessLevel.PRIVATE)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String url;

    private String caption;

    private Float lat;

    private Float lng;

    //todo: get rid of @JsonBackReference
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Setter(AccessLevel.PRIVATE)
    private List<Comment> comments = new ArrayList<>();

    public void addComment(Comment comment){
        comment.setPost(this);
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
}






















