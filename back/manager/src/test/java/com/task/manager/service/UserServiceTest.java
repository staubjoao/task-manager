package com.task.manager.service;

import com.task.manager.domain.User;
import com.task.manager.gateway.request.UpdateUserRequest;
import com.task.manager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .build();
    }

    @Test
    void saveUser_ShouldEncodePasswordAndSaveUser() {
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.saveUser(user);

        assertNotNull(savedUser);
        assertEquals("encodedPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getUserById_ShouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> users = userService.getAllUsers();

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
    }

    @Test
    void findByEmail_ShouldReturnOptionalUser() {
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findByEmail("john.doe@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals(user.getEmail(), foundUser.get().getEmail());
    }

    @Test
    void updateUser_ShouldUpdateUserFields() {
        UpdateUserRequest updateRequest = new UpdateUserRequest("Jane", "Smith");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.updateUser(1L, updateRequest);

        assertNotNull(updatedUser);
        assertEquals("Jane", updatedUser.getFirstName());
        assertEquals("Smith", updatedUser.getLastName());
        verify(userRepository, times(1)).save(user);
    }
}
