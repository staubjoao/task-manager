package com.task.manager.integration;

import tools.jackson.databind.ObjectMapper;
import com.task.manager.domain.User;
import com.task.manager.domain.enuns.Role;
import com.task.manager.gateway.request.AuthRequest;
import com.task.manager.repository.ProjectRepository;
import com.task.manager.repository.TaskRepository;
import com.task.manager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();

        User user = userRepository.save(User.builder()
                .firstName("Test")
                .lastName("User")
                .email("test@example.com")
                .password(passwordEncoder.encode("password"))
                .role(Role.MEMBER)
                .build());
    }

    @Test
    void login_ShouldReturnToken_WhenCredentialsAreValid() throws Exception {
        AuthRequest authRequest = new AuthRequest("test@example.com", "password");

        mockMvc.perform(post("/public/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void login_ShouldReturnUnauthorized_WhenCredentialsAreInvalid() throws Exception {
        AuthRequest authRequest = new AuthRequest("test@example.com", "wrong-password");

        mockMvc.perform(post("/public/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isUnauthorized());
    }
}
