package com.example.instcrud.service;

import com.example.instcrud.dto.UserDTO;
import com.example.instcrud.dto.UserDTOMapper;
import com.example.instcrud.entity.User;
import com.example.instcrud.exception.UserNotFoundException;
import com.example.instcrud.exception.UserPropertyValueException;
import com.example.instcrud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<UserDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .stream()
                .map(userDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public User save(User user) {

        //username, status and password must not be null
        Optional.ofNullable(user.getUsername())
                .orElseThrow(() -> new UserPropertyValueException("not-null property references a null : "
                        + "User.username"));

        Optional.ofNullable(user.getStatus())
                .orElseThrow(() -> new UserPropertyValueException("not-null property references a null : "
                        + "User.status"));

        Optional.ofNullable(user.getPassword())
                .orElseThrow(() -> new UserPropertyValueException("not-null property references a null : "
                        + "User.password"));

        //at least phone or email must not be null
        Optional.ofNullable(user.getPhone())
                .or(() -> Optional.ofNullable(user.getEmail()))
                .orElseThrow(() -> new UserPropertyValueException("not-null property references a null : "
                        + "User.username or User.email"));

        return userRepository.save(user);
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
