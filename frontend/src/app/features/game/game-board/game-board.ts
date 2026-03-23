import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MatchService } from '../../../core/services/match';

@Component({
  selector: 'app-game-board',
  standalone: true,
  templateUrl: './game-board.html'
})
export class GameBoard {

  private route = inject(ActivatedRoute);
  private matchService = inject(MatchService);

  match = signal<any>(null);

  constructor() {
    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
      this.loadMatch(Number(id));
    }
  }

  loadMatch(id: number) {
    this.matchService.getMatchById(id).subscribe({
      next: (match) => {
        this.match.set(match);
      }
    });
  }
}