package com.task.manager.service;

import com.task.manager.domain.User;
import com.task.manager.gateway.request.UpdateUserRequest;
import com.task.manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersByIds(List<Long> ids) {
        return userRepository.findAllById(ids);
    }

    public User updateUser(Long id, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setFirstName(updateUserRequest.firstName());
            user.setLastName(updateUserRequest.lastName());
            return userRepository.save(user);
        }
        return  null;
    }

}
