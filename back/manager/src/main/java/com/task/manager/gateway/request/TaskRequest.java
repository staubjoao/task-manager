package com.task.manager.gateway.request;

import com.task.manager.domain.enuns.TaskPriority;
import com.task.manager.domain.enuns.TaskStatus;

public record TaskRequest (
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        Long assigneeId,
        Long projectId
) {
}
