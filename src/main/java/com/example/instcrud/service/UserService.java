package com.example.instcrud.service;

import com.example.instcrud.dto.user.UserResponseDTO;
import com.example.instcrud.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UserResponseDTO findById(long id);
    User save(User user);

    List<UserResponseDTO> findAll(Pageable pageable);

    boolean existsById(Long id);
    void deleteById(Long id);
}
