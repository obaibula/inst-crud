package com.example.instcrud.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // todo: when LocalDateTime properties are Null, when insert into db, they must be set to DEFAULT -> now()
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // todo: alter table, add UNIQUE constraint
    @Column(nullable = false)
    private String username;

    private String bio;

    private String avatar;

    private String phone;

    private String email;

    // todo: alter table, add constraint to not null
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;
}
