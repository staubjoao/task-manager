package com.task.manager.gateway.translator;

import com.task.manager.domain.Project;
import com.task.manager.domain.Task;
import com.task.manager.domain.User;
import com.task.manager.domain.enuns.TaskStatus;
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
                   .status(taskRequest.status() != null ? taskRequest.status() : TaskStatus.TODO)
                   .priority(taskRequest.priority())
                   .dueDate(taskRequest.dueDate())
                   .assignee(assignee)
                   .project(project)
                   .build();
    }

    public TaskResponse toResponse(Task task) {
        if (task == null) return null;
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus() != null ? task.getStatus() : TaskStatus.TODO,
                task.getPriority(),
                task.getDueDate(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                userTranslator.toResponse(task.getAssignee()),
                projectTranslator.toResponse(task.getProject())
        );
    }

}
