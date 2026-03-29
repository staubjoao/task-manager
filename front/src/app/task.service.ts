import { HttpClient } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';
import { TaskResponse, TaskStatus } from './models';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private readonly apiUrl = 'http://localhost:8080/api/task';
  tasks = signal<TaskResponse[]>([]);

  constructor(private http: HttpClient) {}

  loadTasks() {
    this.http.get<TaskResponse[]>(this.apiUrl).subscribe(tasks => {
      this.tasks.set(tasks);
    });
  }

  updateStatus(taskId: number, status: TaskStatus) {
    return this.http.patch<TaskResponse>(`${this.apiUrl}/${taskId}/status`, { status }).subscribe(updatedTask => {
      this.tasks.update(tasks => tasks.map(t => t.id === updatedTask.id ? updatedTask : t));
    });
  }
}
