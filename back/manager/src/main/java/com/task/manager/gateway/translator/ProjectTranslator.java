package com.task.manager.gateway.translator;

import com.task.manager.domain.Project;
import com.task.manager.domain.User;
import com.task.manager.gateway.request.ProjectRequest;
import com.task.manager.gateway.response.ProjectResponse;
import com.task.manager.gateway.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjectTranslator {

    private final UserTranslator userTranslator;

    public Project toDomain(ProjectRequest projectRequest, List<User> members) {
        return Project.builder()
                      .name(projectRequest.name())
                      .description(projectRequest.description())
                      .members(members)
                      .build();
    }

    public ProjectResponse toResponse(Project project) {
        List<UserResponse> members = project.getMembers()
                                            .stream()
                                            .map(userTranslator::toResponse)
                                            .toList();
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                members
        );
    }
}
