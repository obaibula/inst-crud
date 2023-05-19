package com.example.instcrud.service;

import com.example.instcrud.dto.UserDTO;
import com.example.instcrud.entity.User;

public interface UserService {
    UserDTO findById(long id);
    User save(User user);
}
