package com.example.instcrud.service;

import com.example.instcrud.dto.UserDTO;
import com.example.instcrud.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface UserService {
    UserDTO findById(long id);
    User save(User user);

    void updateUser(Long userId, Map<String, Object> updates);

    List<UserDTO> findAll(Pageable pageable);

    void deleteById(Long id);
}
