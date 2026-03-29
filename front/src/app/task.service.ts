import { HttpClient } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';
import { TaskResponse, TaskStatus, TaskRequest } from './models';
import { tap } from 'rxjs/operators';

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

  createTask(request: TaskRequest) {
    return this.http.post<TaskResponse>(this.apiUrl, request).pipe(
      tap(newTask => {
        this.tasks.update(tasks => [newTask, ...tasks]);
      })
    );
  }

  updateStatus(taskId: number, status: TaskStatus) {
    return this.http.patch<TaskResponse>(`${this.apiUrl}/${taskId}/status`, `"${status}"`, {
      headers: { 'Content-Type': 'application/json' }
    }).subscribe(updatedTask => {
      this.tasks.update(tasks => tasks.map(t => t.id === updatedTask.id ? updatedTask : t));
    });
  }
}
