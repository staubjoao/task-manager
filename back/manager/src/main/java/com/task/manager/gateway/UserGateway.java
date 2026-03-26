package com.task.manager.gateway;

import com.task.manager.domain.User;
import com.task.manager.gateway.request.NewUserRequest;
import com.task.manager.gateway.request.UpdateUserRequest;
import com.task.manager.gateway.response.UserResponse;
import com.task.manager.gateway.translator.UserTranslator;
import com.task.manager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserGateway {

    private final UserService userService;
    private final UserTranslator userTranslator;

    public UserResponse save(NewUserRequest newUserRequest) {
        User user = userTranslator.toDomain(newUserRequest);
        return userTranslator.toResponse(userService.saveUser(user));
    }

    public UserResponse findById(Long id) {
        User user = userService.getUserById(id);
        return userTranslator.toResponse(user);
    }

    public List<UserResponse> findAll() {
        return userService.getAllUsers().stream()
                          .map(userTranslator::toResponse).toList();
    }

    public UserResponse update(Long id, UpdateUserRequest updateUserRequest) {
        return userTranslator.toResponse(userService.updateUser(id, updateUserRequest));
    }

}
