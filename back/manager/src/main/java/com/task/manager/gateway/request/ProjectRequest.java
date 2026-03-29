package com.task.manager.gateway.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ProjectRequest(
        @NotEmpty
        String name,
        @NotEmpty
        String description,
        @NotEmpty
        List<Long> memberIds
) {
}
