package com.task.manager.gateway.request;

import java.util.List;

public record ProjectRequest(
        String name,
        String description,
        List<Long> memberIds
) {
}
