import { Component, inject, signal, computed } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../auth.service';
import { ToastService } from '../toast.service';
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
  private toastService = inject(ToastService);

  firstName = signal('');
  lastName = signal('');
  email = signal('');
  password = signal('');
  role = signal(Role.MEMBER);
  loading = signal(false);
  error = signal<string | null>(null);

  isFormValid = computed(() => {
    return (
      this.firstName().trim() !== '' &&
      this.lastName().trim() !== '' &&
      this.validateEmail(this.email()) &&
      this.password().length >= 6
    );
  });

  onSubmit(event: Event) {
    event.preventDefault();
    if (!this.isFormValid()) return;

    this.loading.set(true);
    this.error.set(null);

    this.authService.register({
      firstName: this.firstName(),
      lastName: this.lastName(),
      email: this.email(),
      password: this.password(),
      role: this.role()
    }).subscribe({
      next: () => {
        this.toastService.success('Conta criada com sucesso!');
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.error.set('Falha ao registrar');
        this.toastService.error('Erro ao criar conta. Tente novamente.');
        this.loading.set(false);
      }
    });
  }

  private validateEmail(email: string): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }
}
