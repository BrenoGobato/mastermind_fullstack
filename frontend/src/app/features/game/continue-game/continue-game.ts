import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatchService } from '../../../core/services/match';
import { AuthService } from '../../../core/services/auth';
import { Match } from '../../../shared/models/match-model';

@Component({
  selector: 'app-continue-game',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './continue-game.html',
  styleUrl: './continue-game.css'
})
export class ContinueGame {
  private matchService = inject(MatchService);
  private authService = inject(AuthService);
  private router = inject(Router);

  matches = signal<Match[]>([]);
  loading = signal(true);
  errorMessage = signal('');

  constructor() {
    this.loadMatches();
  }

  loadMatches() {
    const user = this.authService.user();

    if (!user) {
      this.errorMessage.set('Unauthenticated user');
      this.loading.set(false);
      return;
    }

    this.matchService.getInProgressMatches(user.id).subscribe({
      next: (matches) => {
        this.matches.set(matches);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Error loading matches in progress', err);
        this.errorMessage.set('We were unable to load matches');
        this.loading.set(false);
      }
    });
  }

  openMatch(matchId: number) {
    this.router.navigate([`/game/${matchId}`]);
  }

  goBack() {
    this.router.navigate(['/dashboard']);
  }
}
