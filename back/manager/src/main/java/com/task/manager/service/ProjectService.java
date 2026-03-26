package com.task.manager.service;

import com.task.manager.domain.Project;
import com.task.manager.domain.Task;
import com.task.manager.repository.ProjectRepository;
import com.task.manager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
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
