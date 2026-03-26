package com.task.manager.gateway.request;

public record UpdateUserRequest (
        String firstName,
        String lastName
) {
}
