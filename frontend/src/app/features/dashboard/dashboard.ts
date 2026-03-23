import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  templateUrl: './dashboard.html'
})
export class Dashboard {

  private router = inject(Router);
  private authService = inject(AuthService);

  user = this.authService.user;

  newGame() {
    this.router.navigate(['/game/new']);
  }

  continueGame() {
    this.router.navigate(['/continue']);
  }

  goToRanking() {
    this.router.navigate(['/ranking']);
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}