package com.task.manager.controller;

import com.task.manager.gateway.TaskGateway;
import com.task.manager.gateway.request.ProjectRequest;
import com.task.manager.gateway.request.TaskRequest;
import com.task.manager.gateway.response.ProjectResponse;
import com.task.manager.gateway.response.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskGateway taskGateway;

    @GetMapping
    public List<TaskResponse> getAllTasks() {
        return taskGateway.findAll();
    }

    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id) {
        return taskGateway.findById(id);
    }

    @PostMapping
    public TaskResponse createTask(@RequestBody TaskRequest taskRequest) {
        return taskGateway.save(taskRequest);
    }


    @PutMapping("/{id}")
    public TaskResponse updateTask(@PathVariable Long id, @RequestBody TaskRequest taskRequest) {
        return taskGateway.update(id, taskRequest);
    }
}
