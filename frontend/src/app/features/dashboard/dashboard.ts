import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth';
import { MatchService } from '../../core/services/match';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard {

  private router = inject(Router);
  private authService = inject(AuthService);

  user = this.authService.user;

  private matchService = inject(MatchService);

  newGame() {
    const user = this.authService.user();

    if (!user) {
      return;
    }

    this.matchService.createMatch(user.id).subscribe({
      next: (match) => {
        this.router.navigate([`/game/${match.id}`]);
      },
      error: (err) => {
        console.error('Erro ao criar partida', err);
      }
    });
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