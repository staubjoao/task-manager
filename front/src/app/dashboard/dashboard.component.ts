import { Component, inject, OnInit, signal, computed } from '@angular/core';
import { ProjectService } from '../project.service';
import { TaskService } from '../task.service';
import { UserService } from '../user.service';
import { AuthService } from '../auth.service';
import { ToastService } from '../toast.service';
import { TaskStatus, Role, ProjectResponse, TaskPriority, TaskRequest } from '../models';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  projectService = inject(ProjectService);
  taskService = inject(TaskService);
  userService = inject(UserService);
  authService = inject(AuthService);
  toastService = inject(ToastService);

  showProjectForm = signal(false);
  editingProjectId = signal<number | null>(null);
  newProject = { name: signal(''), description: signal(''), memberIds: signal<number[]>([]) };

  showTaskForm = signal(false);
  newTask = {
    title: signal(''),
    description: signal(''),
    priority: signal(TaskPriority.MEDIUM),
    dueDate: signal(''),
    assigneeId: signal<number | null>(null),
    projectId: signal<number | null>(null)
  };

  priorities = Object.values(TaskPriority);

  isProjectFormValid = computed(() => {
    return this.newProject.name().trim().length >= 3;
  });

  isTaskFormValid = computed(() => {
    return (
      this.newTask.title().trim().length >= 3 &&
      this.newTask.assigneeId() !== null &&
      this.newTask.projectId() !== null &&
      this.newTask.dueDate() !== ''
    );
  });

  ngOnInit() {
    this.projectService.loadProjects();
    this.taskService.loadTasks();
    if (this.authService.isAdmin()) {
      this.userService.loadUsers();
    }
  }

  toggleProjectForm() {
    if (this.showProjectForm()) {
      this.resetProjectForm();
    } else {
      this.showProjectForm.set(true);
      this.showTaskForm.set(false);
    }
  }

  toggleTaskForm() {
    if (this.showTaskForm()) {
      this.resetTaskForm();
    } else {
      this.showTaskForm.set(true);
      this.showProjectForm.set(false);
    }
  }

  resetProjectForm() {
    this.showProjectForm.set(false);
    this.editingProjectId.set(null);
    this.newProject.name.set('');
    this.newProject.description.set('');
    this.newProject.memberIds.set([]);
  }

  resetTaskForm() {
    this.showTaskForm.set(false);
    this.newTask.title.set('');
    this.newTask.description.set('');
    this.newTask.priority.set(TaskPriority.MEDIUM);
    this.newTask.dueDate.set('');
    this.newTask.assigneeId.set(null);
    this.newTask.projectId.set(null);
  }

  isMemberSelected(userId: number): boolean {
    return this.newProject.memberIds().includes(userId);
  }

  toggleMember(userId: number) {
    const current = this.newProject.memberIds();
    if (current.includes(userId)) {
      this.newProject.memberIds.set(current.filter(id => id !== userId));
    } else {
      this.newProject.memberIds.set([...current, userId]);
    }
  }

  startEditProject(project: ProjectResponse) {
    this.editingProjectId.set(project.id);
    this.newProject.name.set(project.name);
    this.newProject.description.set(project.description);
    this.newProject.memberIds.set(project.members.map(m => m.id));
    this.showProjectForm.set(true);
    this.showTaskForm.set(false);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  saveProject() {
    if (!this.isProjectFormValid()) return;

    const projectData = {
      name: this.newProject.name(),
      description: this.newProject.description(),
      memberIds: this.newProject.memberIds()
    };

    const id = this.editingProjectId();
    if (id) {
      this.projectService.updateProject(id, projectData).subscribe({
        next: () => {
          this.toastService.success('Projeto atualizado com sucesso!');
          this.resetProjectForm();
        },
        error: () => this.toastService.error('Erro ao atualizar projeto')
      });
    } else {
      this.projectService.createProject(projectData).subscribe({
        next: () => {
          this.toastService.success('Projeto criado com sucesso!');
          this.resetProjectForm();
        },
        error: () => this.toastService.error('Erro ao criar projeto')
      });
    }
  }

  saveTask() {
    if (!this.isTaskFormValid()) return;

    const taskData: TaskRequest = {
      title: this.newTask.title(),
      description: this.newTask.description(),
      priority: this.newTask.priority(),
      dueDate: this.newTask.dueDate(),
      assigneeId: this.newTask.assigneeId()!,
      projectId: this.newTask.projectId()!
    };

    this.taskService.createTask(taskData).subscribe({
      next: () => {
        this.toastService.success('Tarefa criada com sucesso!');
        this.resetTaskForm();
      },
      error: () => this.toastService.error('Erro ao criar tarefa')
    });
  }

  updateStatus(taskId: number, status: string) {
    this.taskService.updateStatus(taskId, status as TaskStatus);
    this.toastService.success(`Status atualizado para ${status}`);
  }

  logout() {
    this.authService.logout();
    this.toastService.info('Sessão encerrada');
  }
}
