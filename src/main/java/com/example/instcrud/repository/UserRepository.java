package com.example.instcrud.repository;

import com.example.instcrud.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long>{

    // This custom query is made to avoid issues connected to the Open Session In View anti-pattern.
    @Query("""
            SELECT u
            FROM User u
            LEFT JOIN FETCH u.posts""")
    Page<User> findAllFetchPosts(Pageable pageable);
}
