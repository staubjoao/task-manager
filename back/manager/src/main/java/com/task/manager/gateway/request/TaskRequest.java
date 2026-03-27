package com.task.manager.gateway.request;

import com.task.manager.domain.enuns.TaskPriority;
import com.task.manager.domain.enuns.TaskStatus;

import java.time.LocalDateTime;

public record TaskRequest(
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        LocalDateTime dueDate,
        Long assigneeId,
        Long projectId
) {
}
