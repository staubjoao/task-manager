package com.task.manager.gateway.request;

import com.task.manager.domain.enuns.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record NewUserRequest(
        @NotEmpty
        String firstName,
        @NotEmpty
        String lastName,
        @Email
        String email,
        @NotEmpty
        String password,
        @NotEmpty
        Role role
) {
}
