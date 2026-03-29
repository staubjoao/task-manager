import { HttpClient } from '@angular/common/http';
import { Injectable, signal, computed } from '@angular/core';
import { AuthRequest, AuthResponse, NewUserRequest, Role } from './models';
import { tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly apiUrl = 'http://localhost:8080';
  private tokenSignal = signal<string | null>(localStorage.getItem('token'));
  private roleSignal = signal<Role | null>(null);

  token = computed(() => this.tokenSignal());
  isAuthenticated = computed(() => !!this.tokenSignal());
  userRole = computed(() => this.roleSignal());
  isAdmin = computed(() => this.roleSignal() === Role.ADMIN);

  constructor(private http: HttpClient, private router: Router) {
    const initialToken = localStorage.getItem('token');
    if (initialToken) {
      this.roleSignal.set(this.getRoleFromToken(initialToken));
    }
  }

  login(request: AuthRequest) {
    return this.http.post<AuthResponse>(`${this.apiUrl}/public/auth/login`, request).pipe(
      tap(response => {
        this.saveToken(response.token);
      })
    );
  }

  register(request: NewUserRequest) {
    return this.http.post<void>(`${this.apiUrl}/public/register`, request);
  }

  logout() {
    this.clearToken();
    this.router.navigate(['/login']);
  }

  private saveToken(token: string) {
    localStorage.setItem('token', token);
    this.tokenSignal.set(token);
    this.roleSignal.set(this.getRoleFromToken(token));
  }

  private clearToken() {
    localStorage.removeItem('token');
    this.tokenSignal.set(null);
    this.roleSignal.set(null);
  }

  private getRoleFromToken(token: string | null): Role | null {
    if (!token) return null;
    try {
      const decoded: any = jwtDecode(token);
      return decoded.role as Role;
    } catch {
      return null;
    }
  }
}
