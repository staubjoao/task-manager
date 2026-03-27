package com.task.manager.controller;

import com.task.manager.gateway.request.AuthRequest;
import com.task.manager.gateway.response.AuthResponse;
import com.task.manager.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponse authenticate(@RequestBody AuthRequest request) {
        return authService.authenticate(request.email(), request.password());
    }
}
