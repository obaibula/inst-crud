package com.example.instcrud.service;

import com.example.instcrud.dto.UserDTO;
import com.example.instcrud.dto.UserDTOMapper;
import com.example.instcrud.entity.User;
import com.example.instcrud.entity.UserStatus;
import com.example.instcrud.exception.UserNotFoundException;
import com.example.instcrud.exception.UserPropertyValueException;
import com.example.instcrud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserDTOMapper userDTOMapper;

    @Override
    @Transactional(readOnly = true) //to avoid possible dirty checking in Hibernate
    public UserDTO findById(long id) {
        return userRepository.findById(id)
                .map(userDTOMapper)
                .orElseThrow(() -> new UserNotFoundException("Not found user with id - " + id));
    }



    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> findAll(Pageable pageable) {
        return userRepository.findAllFetchPosts(pageable)
                .map(userDTOMapper)
                .toList();
    }

    @Override
    @Transactional
    public User save(User user) {
        checkForNulls(user);
        return userRepository.save(user);
    }

    private void checkForNulls(User user) {
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
    }

    @Override
    @Transactional
    public void updateUser(Long userId, Map<String, Object> updates) {
        var user = findUserOrElseThrow(userId);

        updates.forEach(updateAppropriateFields(user));
        save(user);
    }

    private BiConsumer<String, Object> updateAppropriateFields(User user) {
        return (field, value) -> {
            switch (field) {
                case "username" -> user.setUsername((String) value);
                case "bio" -> user.setBio((String) value);
                case "avatar" -> user.setAvatar((String) value);
                case "phone" -> user.setPhone((String) value);
                case "password" -> user.setPassword((String) value);
                case "status" -> user.setStatus(UserStatus.valueOf((String) value));
            }
        };
    }

    @Override
    public void deleteById(Long userId) {
        findUserOrElseThrow(userId);
        userRepository.deleteById(userId);
    }

    private User findUserOrElseThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Not found user with userId - " + userId));
    }
}
