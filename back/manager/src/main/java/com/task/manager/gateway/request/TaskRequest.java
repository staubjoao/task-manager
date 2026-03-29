package com.task.manager.gateway.request;

import com.task.manager.domain.enuns.TaskPriority;
import com.task.manager.domain.enuns.TaskStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TaskRequest(
        @NotEmpty
        String title,
        @NotEmpty
        String description,
        TaskStatus status,
        @NotNull
        TaskPriority priority,
        @NotNull
        LocalDateTime dueDate,
        @NotNull
        Long assigneeId,
        @NotNull
        Long projectId
) {
}
