package com.task.manager.gateway.response;

import com.task.manager.domain.enuns.TaskPriority;
import com.task.manager.domain.enuns.TaskStatus;

public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        UserResponse assignee,
        ProjectResponse project
) {
}
