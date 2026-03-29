package com.task.manager.gateway.request;

import jakarta.validation.constraints.NotEmpty;

public record AuthRequest(
        @NotEmpty
        String email,
        @NotEmpty
        String password) {
}
