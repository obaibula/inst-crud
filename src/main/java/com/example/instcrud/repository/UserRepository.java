package com.example.instcrud.repository;

import com.example.instcrud.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

public interface UserRepository extends JpaRepository<User, Long>{

    // todo: Something went wrong after the comment entity had been added. Refactor. create custom Repository or change Post JSON
    // This custom query is made to avoid issues connected to the Open Session In View anti-pattern.
    @Query("""
            SELECT u
            FROM User u
            LEFT JOIN FETCH u.posts""")
    Page<User> findAllFetchPosts(Pageable pageable);

    // This method uses native ON DELETE CASCADE, so it would not cause the N+1 issue
    // Don't forget to use @Transactional on an appropriate service method
    // todo: Is there any built-in method in Spring Data for that purpose?
    //  Should I enable automatic flushing or updating? Find out!
    //  Consider the following:
    //  https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.modifying-queries
    @Modifying(flushAutomatically = true)
    @Query(value = """
            DELETE
            FROM User u
            WHERE u.id = ?1
            """)
    void deleteInBulkById(@NonNull Long id);
}
