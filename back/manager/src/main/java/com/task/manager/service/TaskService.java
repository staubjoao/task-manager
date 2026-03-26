package com.task.manager.service;

import com.task.manager.domain.Task;
import com.task.manager.domain.User;
import com.task.manager.domain.enuns.TaskStatus;
import com.task.manager.repository.TaskRepository;
import com.task.manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Task saveTask(Task task) {
        task.setCreatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task updateTask(Long id, Task updatedTask) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task != null) {
            task.setTitle(updatedTask.getTitle());
            task.setDescription(updatedTask.getDescription());
            task.setAssignee(updatedTask.getAssignee());
            task.setUpdatedAt(LocalDateTime.now());
            return taskRepository.save(task);
        }
        return null;
    }

    public Task updateStatus(Long id, TaskStatus newStatus) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task == null) {
            throw new RuntimeException("Task not found with id: " + id);
        }

        validateStatusTransition(task.getStatus(), newStatus);
        //TODO: tratar a regra de que uma task como CRITICAL só pode ser fechada (DONE) por um ADMIN do projeto
    }

    private void validateStatusTransition(TaskStatus currentStatus, TaskStatus newStatus) {
        if (TaskStatus.DONE.equals(currentStatus) && TaskStatus.TODO.equals(newStatus)) {
            // TODO: ajustar isso para uma exception customizada para tratar as respostas ao usuario
            throw new RuntimeException("Cannot change status from DONE to TODO");
        }
    }


}
