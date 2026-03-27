package com.task.manager.gateway.request;

import com.task.manager.domain.enuns.Role;

public record NewUserRequest(
        String firstName,
        String lastName,
        String email,
        String password,
        Role role
) {
}
