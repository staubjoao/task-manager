package com.task.manager.gateway.request;

import jakarta.validation.constraints.NotEmpty;

public record UpdateUserRequest(
        @NotEmpty
        String firstName,
        @NotEmpty
        String lastName
) {
}
