package com.task.manager.controller;

import com.task.manager.gateway.ProjectGateway;
import com.task.manager.gateway.request.ProjectRequest;
import com.task.manager.gateway.response.ProjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectGateway projectGateway;

    @GetMapping
    public List<ProjectResponse> getAllProjects() {
        return projectGateway.findAll();
    }

    @GetMapping("/{id}")
    public ProjectResponse getProjectById(@PathVariable Long id) {
        return projectGateway.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ProjectResponse createProject(@RequestBody ProjectRequest projectRequest) {
        return projectGateway.save(projectRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ProjectResponse updateProject(@PathVariable Long id, @RequestBody ProjectRequest projectRequest) {
        return projectGateway.update(id, projectRequest);
    }
}
