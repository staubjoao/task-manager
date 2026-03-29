package com.task.manager.gateway;

import com.task.manager.gateway.response.AuthResponse;
import com.task.manager.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthGateway {

    private final AuthService authService;

    public AuthResponse authenticate(String email, String password) {
        return authService.authenticate(email, password);
    }

}
