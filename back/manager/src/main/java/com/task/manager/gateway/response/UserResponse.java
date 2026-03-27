package com.task.manager.gateway.response;

import com.task.manager.domain.enuns.Role;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        Role role
) {
}
