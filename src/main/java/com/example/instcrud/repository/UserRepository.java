package com.example.instcrud.repository;

import com.example.instcrud.dto.UserDTO;
import com.example.instcrud.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{

    // todo: Something went wrong after the comment entity had been added. Refactor. create custom Repository or change Post JSON
    // This custom query is made to avoid issues connected to the Open Session In View anti-pattern.
    @Query("""
            SELECT u
            FROM User u
            LEFT JOIN FETCH u.posts""")
    Page<User> findAllFetchPosts(Pageable pageable);
}
