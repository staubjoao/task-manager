package com.task.manager.gateway.request;

public record NewUserRequest(
        String firstName,
        String lastName,
        String email,
        String password
) {
}
