package com.task.manager.service;

import com.task.manager.config.security.SecurityUtils;
import com.task.manager.domain.Project;
import com.task.manager.domain.User;
import com.task.manager.domain.enuns.Role;
import com.task.manager.repository.ProjectRepository;
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
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private ProjectService projectService;

    private User admin;
    private User member;
    private Project project;

    @BeforeEach
    void setUp() {
        admin = User.builder().id(1L).email("admin@test.com").role(Role.ADMIN).build();
        member = User.builder().id(2L).email("member@test.com").role(Role.MEMBER).build();
        project = Project.builder()
                .id(1L)
                .name("Project 1")
                .owner(member)
                .members(new ArrayList<>())
                .build();
    }

    @Test
    void saveProject_ShouldReturnSavedProject() {
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Project savedProject = projectService.saveProject(project);

        assertNotNull(savedProject);
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void getProjectById_ShouldReturnProject_WhenUserIsOwner() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(securityUtils.getCurrentUser()).thenReturn(member);

        Project foundProject = projectService.getProjectById(1L);

        assertNotNull(foundProject);
        assertEquals(project.getId(), foundProject.getId());
    }

    @Test
    void getProjectById_ShouldReturnNull_WhenUserIsNotMemberOrOwner() {
        User otherUser = User.builder().id(3L).role(Role.MEMBER).build();
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(securityUtils.getCurrentUser()).thenReturn(otherUser);

        Project foundProject = projectService.getProjectById(1L);

        assertNull(foundProject);
    }

    @Test
    void getAllProjects_ShouldReturnAllProjects_WhenUserIsAdmin() {
        when(securityUtils.getCurrentUser()).thenReturn(admin);
        when(projectRepository.findAll()).thenReturn(List.of(project));

        List<Project> projects = projectService.getAllProjects();

        assertFalse(projects.isEmpty());
        assertEquals(1, projects.size());
    }

    @Test
    void getAllProjects_ShouldFilterProjects_WhenUserIsMember() {
        Project otherProject = Project.builder().id(2L).owner(admin).members(new ArrayList<>()).build();
        when(securityUtils.getCurrentUser()).thenReturn(member);
        when(projectRepository.findAll()).thenReturn(List.of(project, otherProject));

        List<Project> projects = projectService.getAllProjects();

        assertEquals(1, projects.size());
        assertEquals(project.getId(), projects.get(0).getId());
    }

    @Test
    void updateProject_ShouldUpdateAndReturnProject() {
        Project updatedData = Project.builder().name("Updated Name").description("Updated Desc").members(new ArrayList<>()).build();
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Project updatedProject = projectService.updateProject(1L, updatedData);

        assertEquals("Updated Name", updatedProject.getName());
        assertEquals("Updated Desc", updatedProject.getDescription());
    }
}
