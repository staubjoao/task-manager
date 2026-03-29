package com.task.manager.gateway.request;

import com.task.manager.domain.enuns.TaskPriority;
import com.task.manager.domain.enuns.TaskStatus;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;

public record TaskRequest(
        @NotEmpty
        String title,
        @NotEmpty
        String description,
        @NotEmpty
        TaskStatus status,
        @NotEmpty
        TaskPriority priority,
        @NotEmpty
        LocalDateTime dueDate,
        @NotEmpty
        Long assigneeId,
        @NotEmpty
        Long projectId
) {
}
