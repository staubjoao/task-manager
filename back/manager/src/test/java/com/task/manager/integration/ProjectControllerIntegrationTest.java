package com.task.manager.integration;

import tools.jackson.databind.ObjectMapper;
import com.task.manager.config.security.JwtService;
import com.task.manager.domain.Project;
import com.task.manager.domain.User;
import com.task.manager.domain.enuns.Role;
import com.task.manager.gateway.request.ProjectRequest;
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

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProjectControllerIntegrationTest {

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

    private User admin;
    private User member;
    private String adminToken;
    private String memberToken;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();

        admin = userRepository.save(User.builder()
                .firstName("Admin")
                .lastName("User")
                .email("admin@test.com")
                .password(passwordEncoder.encode("password"))
                .role(Role.ADMIN)
                .build());
        adminToken = "Bearer " + jwtService.generateToken(admin);

        member = userRepository.save(User.builder()
                .firstName("Member")
                .lastName("User")
                .email("member@test.com")
                .password(passwordEncoder.encode("password"))
                .role(Role.MEMBER)
                .build());
        memberToken = "Bearer " + jwtService.generateToken(member);
    }

    @Test
    void createProject_ShouldReturnCreated_WhenUserIsAdmin() throws Exception {
        ProjectRequest projectRequest = new ProjectRequest("New Project", "Description", List.of(member.getId()));

        mockMvc.perform(post("/api/project")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Project"));
    }

    @Test
    void createProject_ShouldReturnForbidden_WhenUserIsMember() throws Exception {
        ProjectRequest projectRequest = new ProjectRequest("New Project", "Description", List.of(member.getId()));

        mockMvc.perform(post("/api/project")
                        .header("Authorization", memberToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllProjects_ShouldReturnListOfProjects() throws Exception {
        Project project = Project.builder().name("Test Project").owner(admin).members(new ArrayList<>()).build();
        projectRepository.save(project);

        mockMvc.perform(get("/api/project")
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
