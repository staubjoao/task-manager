package com.task.manager.gateway.response;

import java.util.List;

public record ProjectResponse(
        Long id,
        String name,
        String description,
        List<UserResponse> members
) {
}
