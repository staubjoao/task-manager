import { Injectable, signal } from '@angular/core';

export type ToastType = 'success' | 'error' | 'info';

export interface Toast {
  message: string;
  type: ToastType;
}

@Injectable({
  providedIn: 'root'
})
export class ToastService {
  private toastSignal = signal<Toast | null>(null);
  toast = this.toastSignal.asReadonly();

  show(message: string, type: ToastType = 'info', duration: number = 3000) {
    this.toastSignal.set({ message, type });
    setTimeout(() => {
      this.toastSignal.set(null);
    }, duration);
  }

  success(message: string) {
    this.show(message, 'success');
  }

  error(message: string) {
    this.show(message, 'error');
  }

  info(message: string) {
    this.show(message, 'info');
  }
}
