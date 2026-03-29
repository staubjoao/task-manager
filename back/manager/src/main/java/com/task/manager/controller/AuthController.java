package com.task.manager.controller;

import com.task.manager.gateway.AuthGateway;
import com.task.manager.gateway.request.AuthRequest;
import com.task.manager.gateway.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthGateway authGateway;

    @PostMapping("/login")
    public AuthResponse authenticate(@RequestBody AuthRequest request) {
        return authGateway.authenticate(request.email(), request.password());
    }
}
