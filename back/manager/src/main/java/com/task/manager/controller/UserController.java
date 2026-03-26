package com.task.manager.controller;

import com.task.manager.gateway.UserGateway;
import com.task.manager.gateway.request.UpdateUserRequest;
import com.task.manager.gateway.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class UserController {

    private final UserGateway userGateway;

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userGateway.findAll();
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return userGateway.findById(id);
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest updateUserRequest) {
        return userGateway.update(id, updateUserRequest);
    }

}
