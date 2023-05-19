package com.example.instcrud.service;

import com.example.instcrud.dto.UserDTO;
import com.example.instcrud.dto.UserDTOMapper;
import com.example.instcrud.entity.User;
import com.example.instcrud.exception.UserNotFoundException;
import com.example.instcrud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserDTOMapper userDTOMapper;

    @Override
    public UserDTO findById(long id) {
        return userRepository.findById(id)
                .map(userDTOMapper)
                .orElseThrow(() -> new UserNotFoundException("Not found user with id - " + id));
    }

    @Override
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }
}
