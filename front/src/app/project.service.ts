import { HttpClient } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';
import { ProjectResponse, ProjectRequest } from './models';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  private readonly apiUrl = 'http://localhost:8080/api/project';
  projects = signal<ProjectResponse[]>([]);

  constructor(private http: HttpClient) {}

  loadProjects() {
    this.http.get<ProjectResponse[]>(this.apiUrl).subscribe(projects => {
      this.projects.set(projects);
    });
  }

  createProject(request: ProjectRequest) {
    return this.http.post<ProjectResponse>(this.apiUrl, request).pipe(
      tap(newProject => {
        this.projects.update(projects => [...projects, newProject]);
      })
    );
  }

  updateProject(id: number, request: ProjectRequest) {
    return this.http.put<ProjectResponse>(`${this.apiUrl}/${id}`, request).pipe(
      tap(updatedProject => {
        this.projects.update(projects => projects.map(p => p.id === id ? updatedProject : p));
      })
    );
  }
}
