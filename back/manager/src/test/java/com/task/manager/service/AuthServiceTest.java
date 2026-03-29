package com.task.manager.service;

import com.task.manager.config.security.JwtService;
import com.task.manager.domain.User;
import com.task.manager.gateway.response.AuthResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("john.doe@example.com")
                .password("password")
                .build();
    }

    @Test
    void authenticate_ShouldReturnAuthResponse_WhenCredentialsAreValid() {
        when(userService.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        AuthResponse response = authService.authenticate("john.doe@example.com", "password");

        assertNotNull(response);
        assertEquals("jwt-token", response.token());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void authenticate_ShouldThrowException_WhenUserNotFound() {
        when(userService.findByEmail("john.doe@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            authService.authenticate("john.doe@example.com", "password");
        });
    }
}
