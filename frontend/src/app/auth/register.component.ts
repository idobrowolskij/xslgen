import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from './auth.service';

@Component({
  standalone: true,
  selector: 'app-register',
  imports: [CommonModule, FormsModule, RouterLink],
  template: `
  <div class="min-h-screen flex items-center justify-center bg-gray-100">
      <div class="w-full max-w-md bg-white rounded-xl shadow-lg p-8">
        <h2 class="text-2xl font-semibold text-center mb-6">Register</h2>

        <form (ngSubmit)="submit()" class="space-y-4">
          <input
            [(ngModel)]="username"
            name="username"
            placeholder="Username"
            class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          />

          <input
            [(ngModel)]="password"
            name="password"
            type="password"
            placeholder="Password (min. 6)"
            class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          />

          <button
            type="submit"
            class="w-full bg-blue-600 hover:bg-blue-700 text-white font-semibold py-2 rounded-lg transition"
          >
            Register
          </button>
        </form>

        <p *ngIf="error" class="text-red-500 text-sm mt-4 text-center">
          {{ error }}
        </p>

        <p class="text-center text-sm mt-6">
          Schon registriert?
          <a routerLink="/login" class="text-blue-600 hover:underline">
            Zum Login
          </a>
        </p>
      </div>
    </div>
  `
})
export class RegisterComponent {
  username = '';
  password = '';
  error: string | null = null;

  constructor(private auth: AuthService, private router: Router) {}

  submit() {
    this.error = null;
    this.auth.register(this.username, this.password).subscribe({
      next: () => this.router.navigateByUrl('/home'),
      error: () => this.error = 'Register failed'
    });
  }
}