package com.task.manager.gateway.translator;

import com.task.manager.domain.Project;
import com.task.manager.domain.Task;
import com.task.manager.domain.User;
import com.task.manager.gateway.request.TaskRequest;
import com.task.manager.gateway.response.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskTranslator {

    private final UserTranslator userTranslator;
    private final ProjectTranslator projectTranslator;

    public Task toDomain(TaskRequest taskRequest, User assignee, Project project) {
        return Task.builder()
                   .title(taskRequest.title())
                   .description(taskRequest.description())
                   .status(taskRequest.status())
                   .priority(taskRequest.priority())
                   .assignee(assignee)
                   .project(project)
                   .build();
    }

    public TaskResponse toResponse(Task taskRequest) {
        return new TaskResponse(
                taskRequest.getId(),
                taskRequest.getTitle(),
                taskRequest.getDescription(),
                taskRequest.getStatus(),
                taskRequest.getPriority(),
                userTranslator.toResponse(taskRequest.getAssignee()),
                projectTranslator.toResponse(taskRequest.getProject())
        );
    }
}
