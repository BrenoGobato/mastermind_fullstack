import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { RankingService } from '../../core/services/ranking';
import { RankingItem } from '../../shared/models/ranking-model';

@Component({
  selector: 'app-ranking',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './ranking.html',
  styleUrl: './ranking.css'
})
export class Ranking {
  private rankingService = inject(RankingService);
  private router = inject(Router);

  ranking = signal<RankingItem[]>([]);
  loading = signal(true);
  errorMessage = signal('');

  constructor() {
    this.loadRanking();
  }

  loadRanking() {
    this.rankingService.getRanking().subscribe({
      next: (data) => {
        this.ranking.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Error loading ranking', err);
        this.errorMessage.set('We were unable to load ranking.');
        this.loading.set(false);
      }
    });
  }

  formatDuration(seconds: number): string {
    const minutes = Math.floor(seconds / 60);
    const remainingSeconds = seconds % 60;

    return `${minutes}m ${remainingSeconds}s`;
  }

  goBack() {
    this.router.navigate(['/dashboard']);
  }
}