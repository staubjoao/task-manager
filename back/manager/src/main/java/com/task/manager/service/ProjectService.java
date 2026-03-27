package com.task.manager.service;

import com.task.manager.config.security.SecurityUtils;
import com.task.manager.domain.Project;
import com.task.manager.domain.Task;
import com.task.manager.domain.enuns.Role;
import com.task.manager.repository.ProjectRepository;
import com.task.manager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final SecurityUtils securityUtils;

    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    public Project getProjectById(Long id) {
        Project project = projectRepository.findById(id).orElse(null);
        if (project != null && !isUserMemberOrOwner(project)) {
            return null;
        }
        return project;
    }

    public List<Project> getAllProjects() {
        var currentUser = securityUtils.getCurrentUser();
        if (currentUser.getRole() == Role.ADMIN) {
            return projectRepository.findAll();
        }
        return projectRepository.findAll().stream()
                                .filter(this::isUserMemberOrOwner)
                                .toList();
    }

    private boolean isUserMemberOrOwner(Project project) {
        var currentUser = securityUtils.getCurrentUser();
        if (currentUser.getRole() == Role.ADMIN) return true;
        return project.getOwner().getId().equals(currentUser.getId()) ||
               project.getMembers().stream().anyMatch(m -> m.getId().equals(currentUser.getId()));
    }

    public Project updateProject(Long id, Project updatedProject) {
        Project existingProject = projectRepository.findById(id)
                                                   .orElseThrow(() -> new RuntimeException(
                                                           "Project not found with id: " + id));

        existingProject.setName(updatedProject.getName());
        existingProject.setDescription(updatedProject.getDescription());

        if (updatedProject.getMembers() != null) {
            existingProject.getMembers().clear();
            existingProject.getMembers().addAll(updatedProject.getMembers());
        }

        return projectRepository.save(existingProject);
    }

}
