package com.task.manager.gateway.response;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email
) {
}
