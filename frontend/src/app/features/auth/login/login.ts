import { Component, inject, signal } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.html',
  imports: [CommonModule, ReactiveFormsModule, RouterLink]
})
export class Login {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  errorMessage = signal('');

  readonly form = this.fb.nonNullable.group({
    username: ['', [Validators.required]],
    password: ['', [Validators.required]]
  });

  login() {
    console.log('submit disparado');
    console.log('form válido?', this.form.valid);
    console.log('valor', this.form.getRawValue());

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.authService.login(this.form.getRawValue()).subscribe({
      next: (user) => {
        console.log('login OK', user);
        this.authService.setUser(user);
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        console.error('erro login', err);
        this.errorMessage.set('Usuário ou senha inválidos');
      }
    });
  }
}