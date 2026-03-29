import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../auth.service';
import { FormsModule } from '@angular/forms';
import { Role } from '../models';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  firstName = '';
  lastName = '';
  email = '';
  password = '';
  role = Role.MEMBER;
  loading = signal(false);
  error = signal<string | null>(null);

  onSubmit(event: Event) {
    event.preventDefault();
    this.loading.set(true);
    this.error.set(null);

    this.authService.register({
      firstName: this.firstName,
      lastName: this.lastName,
      email: this.email,
      password: this.password,
      role: this.role
    }).subscribe({
      next: () => {
        alert('Registered successfully!');
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.error.set('Registration failed');
        this.loading.set(false);
      }
    });
  }
}
