import { Component, inject, OnInit, signal } from '@angular/core';
import { ProjectService } from '../project.service';
import { TaskService } from '../task.service';
import { UserService } from '../user.service';
import { AuthService } from '../auth.service';
import { TaskStatus, Role, ProjectResponse } from '../models';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  projectService = inject(ProjectService);
  taskService = inject(TaskService);
  userService = inject(UserService);
  authService = inject(AuthService);

  showProjectForm = signal(false);
  editingProjectId = signal<number | null>(null);
  newProject = { name: '', description: '', memberIds: [] as number[] };

  ngOnInit() {
    this.projectService.loadProjects();
    this.taskService.loadTasks();
    if (this.authService.isAdmin()) {
      this.userService.loadUsers();
    }
  }

  toggleForm() {
    if (this.showProjectForm()) {
      this.resetForm();
    } else {
      this.showProjectForm.set(true);
    }
  }

  resetForm() {
    this.showProjectForm.set(false);
    this.editingProjectId.set(null);
    this.newProject = { name: '', description: '', memberIds: [] as number[] };
  }

  isMemberSelected(userId: number): boolean {
    return this.newProject.memberIds.includes(userId);
  }

  toggleMember(userId: number) {
    const idx = this.newProject.memberIds.indexOf(userId);
    if (idx > -1) {
      this.newProject.memberIds.splice(idx, 1);
    } else {
      this.newProject.memberIds.push(userId);
    }
  }

  startEdit(project: ProjectResponse) {
    this.editingProjectId.set(project.id);
    this.newProject = {
      name: project.name,
      description: project.description,
      memberIds: project.members.map(m => m.id)
    };
    this.showProjectForm.set(true);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  saveProject() {
    if (!this.newProject.name) return;

    const id = this.editingProjectId();
    if (id) {
      this.projectService.updateProject(id, this.newProject).subscribe(() => {
        this.resetForm();
      });
    } else {
      this.projectService.createProject(this.newProject).subscribe(() => {
        this.resetForm();
      });
    }
  }

  updateStatus(taskId: number, status: string) {
    this.taskService.updateStatus(taskId, status as TaskStatus);
  }

  logout() {
    this.authService.logout();
  }
}
