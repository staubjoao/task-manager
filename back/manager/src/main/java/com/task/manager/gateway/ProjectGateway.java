package com.task.manager.gateway;

import com.task.manager.domain.Project;
import com.task.manager.domain.Task;
import com.task.manager.domain.User;
import com.task.manager.gateway.request.ProjectRequest;
import com.task.manager.gateway.request.TaskRequest;
import com.task.manager.gateway.response.ProjectResponse;
import com.task.manager.gateway.response.TaskResponse;
import com.task.manager.gateway.translator.ProjectTranslator;
import com.task.manager.service.ProjectService;
import com.task.manager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjectGateway {

    private final ProjectService projectService;
    private final ProjectTranslator projectTranslator;
    private final UserService userService;

    public ProjectResponse save(ProjectRequest projectRequest) {
        Project project = toDomain(projectRequest);
        return projectTranslator.toResponse(projectService.saveProject(project));
    }

    public ProjectResponse findById(Long id) {
        Project project = projectService.getProjectById(id);
        return projectTranslator.toResponse(project);
    }

    public List<ProjectResponse> findAll() {
        return projectService.getAllProjects().stream()
                          .map(projectTranslator::toResponse).toList();
    }

    public ProjectResponse update(Long id, ProjectRequest projectRequest) {
        Project newProject = toDomain(projectRequest);
        return projectTranslator.toResponse(projectService.updateProject(id, newProject));
    }

    private Project toDomain(ProjectRequest projectRequest) {
        List<User> members = userService.getUsersByIds(projectRequest.memberIds());
        return projectTranslator.toDomain(projectRequest, members);
    }
}
