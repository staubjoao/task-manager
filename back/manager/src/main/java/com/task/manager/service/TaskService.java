package com.task.manager.service;

import com.task.manager.config.security.SecurityUtils;
import com.task.manager.domain.Project;
import com.task.manager.domain.Task;
import com.task.manager.domain.User;
import com.task.manager.domain.enuns.Role;
import com.task.manager.domain.enuns.TaskPriority;
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
    private final SecurityUtils securityUtils;

    public Task saveTask(Task task) {
        validateProjectMembership(task.getProject());
        validateAssigneeMembership(task);
        return taskRepository.save(task);
    }

    public Task getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task != null) {
            validateProjectMembership(task.getProject());
        }
        return task;
    }

    public List<Task> getAllTasks() {
        var currentUser = securityUtils.getCurrentUser();
        if (currentUser.getRole() == Role.ADMIN) {
            return taskRepository.findAll();
        }
        return taskRepository.findAll().stream()
                             .filter(task -> isUserMemberOrOwner(task.getProject()))
                             .toList();
    }

    public Task updateTask(Long id, Task updatedTask) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        validateProjectMembership(task.getProject());
        validateAssigneeMembership(updatedTask);

        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setAssignee(updatedTask.getAssignee());
        task.setStatus(updatedTask.getStatus());
        task.setPriority(updatedTask.getPriority());
        task.setDueDate(updatedTask.getDueDate());

        return taskRepository.save(task);
    }

    public Task updateStatus(Long id, TaskStatus newStatus) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        validateProjectMembership(task.getProject());

        validateStatusTransition(task.getStatus(), newStatus);

        if (TaskPriority.CRITICAL.equals(task.getPriority()) && TaskStatus.DONE.equals(newStatus)) {
            var currentUser = securityUtils.getCurrentUser();
            if (currentUser.getRole() != Role.ADMIN &&
                !task.getProject().getOwner().getId().equals(currentUser.getId())) {
                throw new RuntimeException("Only an ADMIN or Project Owner can close a CRITICAL task");
            }
        }

        task.setStatus(newStatus);
        return taskRepository.save(task);
    }

    private void validateStatusTransition(TaskStatus currentStatus, TaskStatus newStatus) {
        if (TaskStatus.DONE.equals(currentStatus) && TaskStatus.TODO.equals(newStatus)) {
            throw new RuntimeException("Cannot change status from DONE to TODO");
        }
    }

    private void validateProjectMembership(Project project) {
        if (!isUserMemberOrOwner(project)) {
            throw new RuntimeException("User is not a member of this project");
        }
    }

    private void validateAssigneeMembership(Task task) {
        if (task.getAssignee() != null) {
            boolean isMember = task.getProject().getMembers().stream()
                                   .anyMatch(m -> m.getId().equals(task.getAssignee().getId()))
                               || task.getProject().getOwner().getId().equals(task.getAssignee().getId());
            if (!isMember) {
                throw new RuntimeException("Assignee must be a member of the project");
            }
        }
    }

    private boolean isUserMemberOrOwner(Project project) {
        var currentUser = securityUtils.getCurrentUser();
        if (currentUser.getRole() == Role.ADMIN) return true;
        return project.getOwner().getId().equals(currentUser.getId()) ||
               project.getMembers().stream().anyMatch(m -> m.getId().equals(currentUser.getId()));
    }
}
