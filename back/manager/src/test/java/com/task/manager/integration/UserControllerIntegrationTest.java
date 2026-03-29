package com.task.manager.integration;

import tools.jackson.databind.ObjectMapper;
import com.task.manager.config.security.JwtService;
import com.task.manager.domain.User;
import com.task.manager.domain.enuns.Role;
import com.task.manager.gateway.request.UpdateUserRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private String token;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();

        user = userRepository.save(User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@test.com")
                .password(passwordEncoder.encode("password"))
                .role(Role.MEMBER)
                .build());
        token = "Bearer " + jwtService.generateToken(user);
    }

    @Test
    void getUserById_ShouldReturnUser() throws Exception {
        mockMvc.perform(get("/api/user/" + user.getId())
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@test.com"));
    }

    @Test
    void updateUser_ShouldUpdateUser() throws Exception {
        UpdateUserRequest updateRequest = new UpdateUserRequest("Jane", "Smith");

        mockMvc.perform(put("/api/user/" + user.getId())
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }
}
