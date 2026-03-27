package com.task.manager.controller;

import com.task.manager.gateway.UserGateway;
import com.task.manager.gateway.request.NewUserRequest;
import com.task.manager.gateway.response.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@AllArgsConstructor
public class CreateUserController {

    private final UserGateway userGateway;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody NewUserRequest newUserRequest) {
        return userGateway.save(newUserRequest);
    }
}
