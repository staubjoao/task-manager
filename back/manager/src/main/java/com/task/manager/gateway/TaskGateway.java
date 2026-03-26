package com.task.manager.gateway;

import com.task.manager.domain.Project;
import com.task.manager.domain.Task;
import com.task.manager.domain.User;
import com.task.manager.gateway.request.TaskRequest;
import com.task.manager.gateway.response.TaskResponse;
import com.task.manager.gateway.translator.TaskTranslator;
import com.task.manager.service.ProjectService;
import com.task.manager.service.TaskService;
import com.task.manager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskGateway {

    private final TaskService taskService;
    private final TaskTranslator taskTranslator;
    private final UserService userService;
    private final ProjectService projectService;

    public TaskResponse save(TaskRequest taskRequest) {
        Task task = toDomain(taskRequest);
        return taskTranslator.toResponse(taskService.saveTask(task));
    }

    public TaskResponse findById(Long id) {
        Task task = taskService.getTaskById(id);
        return taskTranslator.toResponse(task);
    }

    public List<TaskResponse> findAll() {
        return taskService.getAllTasks().stream()
                          .map(taskTranslator::toResponse).toList();
    }

    public TaskResponse update(Long id, TaskRequest taskRequest) {
        Task newTask = toDomain(taskRequest);
        return taskTranslator.toResponse(taskService.updateTask(id, newTask));
    }

    private Task toDomain(TaskRequest taskRequest) {
        User user = userService.getUserById(taskRequest.assigneeId());
        Project project = projectService.getProjectById(taskRequest.projectId());
        return taskTranslator.toDomain(taskRequest, user, project);
    }
}
