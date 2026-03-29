package com.task.manager.service;

import com.task.manager.config.security.SecurityUtils;
import com.task.manager.domain.Project;
import com.task.manager.domain.Task;
import com.task.manager.domain.User;
import com.task.manager.domain.enuns.Role;
import com.task.manager.domain.enuns.TaskPriority;
import com.task.manager.domain.enuns.TaskStatus;
import com.task.manager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private TaskService taskService;

    private User owner;
    private User member;
    private User admin;
    private Project project;
    private Task task;

    @BeforeEach
    void setUp() {
        owner = User.builder().id(1L).email("owner@test.com").role(Role.MEMBER).build();
        member = User.builder().id(2L).email("member@test.com").role(Role.MEMBER).build();
        admin = User.builder().id(3L).email("admin@test.com").role(Role.ADMIN).build();
        project = Project.builder()
                .id(1L)
                .owner(owner)
                .members(new ArrayList<>(List.of(member)))
                .build();
        task = Task.builder()
                .id(1L)
                .title("Task 1")
                .status(TaskStatus.TODO)
                .priority(TaskPriority.MEDIUM)
                .project(project)
                .build();
    }

    @Test
    void saveTask_ShouldSaveTask_WhenUserIsMember() {
        when(securityUtils.getCurrentUser()).thenReturn(member);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task savedTask = taskService.saveTask(task);

        assertNotNull(savedTask);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void saveTask_ShouldThrowException_WhenUserIsNotMember() {
        User otherUser = User.builder().id(4L).role(Role.MEMBER).build();
        when(securityUtils.getCurrentUser()).thenReturn(otherUser);

        assertThrows(RuntimeException.class, () -> taskService.saveTask(task));
    }

    @Test
    void updateStatus_ShouldUpdateStatus_WhenTransitionIsValid() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(securityUtils.getCurrentUser()).thenReturn(member);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task updatedTask = taskService.updateStatus(1L, TaskStatus.IN_PROGRESS);

        assertEquals(TaskStatus.IN_PROGRESS, updatedTask.getStatus());
    }

    @Test
    void updateStatus_ShouldThrowException_WhenTransitionFromDoneToTodo() {
        task.setStatus(TaskStatus.DONE);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(securityUtils.getCurrentUser()).thenReturn(member);

        assertThrows(RuntimeException.class, () -> taskService.updateStatus(1L, TaskStatus.TODO));
    }

    @Test
    void updateStatus_ShouldThrowException_WhenClosingCriticalTaskAsNonOwnerNonAdmin() {
        task.setPriority(TaskPriority.CRITICAL);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(securityUtils.getCurrentUser()).thenReturn(member);

        assertThrows(RuntimeException.class, () -> taskService.updateStatus(1L, TaskStatus.DONE));
    }

    @Test
    void updateStatus_ShouldUpdate_WhenClosingCriticalTaskAsOwner() {
        task.setPriority(TaskPriority.CRITICAL);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(securityUtils.getCurrentUser()).thenReturn(owner);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task updatedTask = taskService.updateStatus(1L, TaskStatus.DONE);

        assertEquals(TaskStatus.DONE, updatedTask.getStatus());
    }

    @Test
    void updateStatus_ShouldUpdate_WhenClosingCriticalTaskAsAdmin() {
        task.setPriority(TaskPriority.CRITICAL);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(securityUtils.getCurrentUser()).thenReturn(admin);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task updatedTask = taskService.updateStatus(1L, TaskStatus.DONE);

        assertEquals(TaskStatus.DONE, updatedTask.getStatus());
    }
}
