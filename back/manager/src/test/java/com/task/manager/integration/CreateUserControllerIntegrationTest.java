package com.task.manager.integration;

import tools.jackson.databind.ObjectMapper;
import com.task.manager.domain.enuns.Role;
import com.task.manager.gateway.request.NewUserRequest;
import com.task.manager.repository.ProjectRepository;
import com.task.manager.repository.TaskRepository;
import com.task.manager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
class CreateUserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void register_ShouldCreateUser() throws Exception {
        NewUserRequest newUserRequest = new NewUserRequest("Jane", "Doe", "jane@test.com", "password", Role.MEMBER);

        mockMvc.perform(post("/public/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("jane@test.com"));
    }
}
