package com.task.manager.integration;

import tools.jackson.databind.ObjectMapper;
import com.task.manager.config.security.JwtService;
import com.task.manager.domain.Project;
import com.task.manager.domain.Task;
import com.task.manager.domain.User;
import com.task.manager.domain.enuns.Role;
import com.task.manager.domain.enuns.TaskPriority;
import com.task.manager.domain.enuns.TaskStatus;
import com.task.manager.gateway.request.TaskRequest;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TaskControllerIntegrationTest {

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

    private User owner;
    private User member;
    private Project project;
    private String ownerToken;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();

        owner = userRepository.save(User.builder()
                .firstName("Owner")
                .email("owner@test.com")
                .password(passwordEncoder.encode("password"))
                .role(Role.MEMBER)
                .build());
        ownerToken = "Bearer " + jwtService.generateToken(owner);

        member = userRepository.save(User.builder()
                .firstName("Member")
                .email("member@test.com")
                .password(passwordEncoder.encode("password"))
                .role(Role.MEMBER)
                .build());

        project = Project.builder()
                .name("Test Project")
                .owner(owner)
                .members(new ArrayList<>(List.of(member)))
                .build();
        projectRepository.save(project);
    }

    @Test
    void createTask_ShouldReturnCreated() throws Exception {
        TaskRequest taskRequest = new TaskRequest(
                "Task Title",
                "Task Description",
                TaskStatus.TODO,
                TaskPriority.MEDIUM,
                LocalDateTime.now().plusDays(1),
                member.getId(),
                project.getId()
        );

        mockMvc.perform(post("/api/task")
                        .header("Authorization", ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task Title"));
    }

    @Test
    void updateStatus_ShouldUpdateStatus() throws Exception {
        Task task = Task.builder()
                .title("Initial Task")
                .status(TaskStatus.TODO)
                .priority(TaskPriority.MEDIUM)
                .project(project)
                .build();
        taskRepository.save(task);

        mockMvc.perform(patch("/api/task/" + task.getId() + "/status")
                        .header("Authorization", ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"IN_PROGRESS\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void updateStatus_ShouldReturnError_WhenInvalidTransition() throws Exception {
        Task task = Task.builder()
                .title("Initial Task")
                .status(TaskStatus.DONE)
                .priority(TaskPriority.MEDIUM)
                .project(project)
                .build();
        taskRepository.save(task);

        mockMvc.perform(patch("/api/task/" + task.getId() + "/status")
                        .header("Authorization", ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"TODO\""))
                .andExpect(status().isInternalServerError());
    }
}
