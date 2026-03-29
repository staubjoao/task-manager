import { HttpClient } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';
import { UserResponse } from './models';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly apiUrl = 'http://localhost:8080/api/user';
  users = signal<UserResponse[]>([]);

  constructor(private http: HttpClient) {}

  loadUsers() {
    this.http.get<UserResponse[]>(this.apiUrl).subscribe(users => {
      this.users.set(users);
    });
  }
}
