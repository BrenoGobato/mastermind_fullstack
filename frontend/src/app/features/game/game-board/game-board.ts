import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MatchService } from '../../../core/services/match';

@Component({
  selector: 'app-game-board',
  standalone: true,
  templateUrl: './game-board.html',
  styleUrl: './game-board.css'
})
export class GameBoard {

  private route = inject(ActivatedRoute);
  private matchService = inject(MatchService);

  router = inject(Router);

  match = signal<any>(null);

  rows = signal<{
    sequence: string[];
    correctPositions: number;
    attemptsLeft: number;
    matchStatus: string;
  }[]>([]);
  
  currentRow = signal<string[]>([]);

  readonly maxAttempts = 10;
  readonly totalSlots = 4;
  readonly totalColors = 8;

  boardIndexes = Array.from({ length: this.maxAttempts }, (_, i) => this.maxAttempts - i);

  slots = [0, 1, 2, 3];

  selectedIndex = signal<number | null>(0);
  manualSelection = signal(false);

  gameStatus = signal<'IN_PROGRESS' | 'VICTORY' | 'DEFEAT'>('IN_PROGRESS');
  correctAnswer = signal<string[] | null>(null);

  colorOptions = [
    'RED',
    'BLUE',
    'GREEN',
    'YELLOW',
    'BLACK',
    'WHITE',
    'ORANGE',
    'BROWN'
  ] as const;

  colorMap: Record<string, string> = {
    RED: 'red',
    BLUE: 'blue',
    GREEN: 'green',
    YELLOW: 'yellow',
    BLACK: 'black',
    WHITE: 'white',
    ORANGE: 'orange',
    BROWN: 'brown'
  };


  constructor() {
    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
      this.loadMatch(Number(id));
    }

    this.currentRow.set(['', '', '', '']);
  }

  loadMatch(id: number) {
    this.matchService.getMatchById(id).subscribe({
      next: (match) => {
        this.match.set(match);
      }
    });
  }

  selectPosition(index: number) {
    if (this.gameStatus() !== 'IN_PROGRESS') return;

    console.log('selecionou posição', index);
    this.selectedIndex.set(index);
  }

  chooseColor(color: string) {
    if (this.gameStatus() !== 'IN_PROGRESS') return;

    console.log('cor clicada', color);

    const index = this.selectedIndex();
    console.log('index atual', index);

    if (index === null) return;

    const updated = [...this.currentRow()];
    updated[index] = color;

    console.log('linha atualizada', updated);

    this.currentRow.set(updated);

    if (index < this.totalSlots - 1) {
      this.selectedIndex.set(index + 1);
    }
  }

  updateCell(index: number, event: Event) {
    const select = event.target as HTMLSelectElement;
    const value = select.value;

    const updated = [...this.currentRow()];
    updated[index] = value;

    this.currentRow.set(updated);
  }

  getAttemptByBoardIndex(boardIndex: number) {
    const rowPosition = this.maxAttempts - boardIndex;
    return this.rows()[rowPosition] ?? null;
  }

  isCurrentRow(boardIndex: number) {
    return boardIndex === this.maxAttempts - this.rows().length;
  }

  getFeedbackCells(correctPositions: number): boolean[] {
    return Array.from({ length: 4 }, (_, i) => i < correctPositions);
  }

  submitGuess() {
    const guess = this.currentRow();
    const matchId = Number(this.route.snapshot.paramMap.get('id'));

    const hasEmpty = guess.some(v => !v);
    const reachedLimit = this.rows().length >= this.maxAttempts;
    const isFinished = this.gameStatus() !== 'IN_PROGRESS';

    if (hasEmpty || reachedLimit || !matchId || isFinished) {
      return;
    }

    this.matchService.submitAttempt(matchId, { sequence: guess }).subscribe({
      next: (response: any) => {
        console.log('✅ resposta backend:', response);

        this.rows.set([
          ...this.rows(),
          {
            sequence: [...guess],
            correctPositions: response.correctPositions,
            attemptsLeft: response.attemptsLeft,
            matchStatus: response.matchStatus
          }
        ]);

        this.gameStatus.set(response.matchStatus);

        if (response.correctAnswer) {
          this.correctAnswer.set(response.correctAnswer);
        }

        if (response.matchStatus === 'IN_PROGRESS') {
          this.currentRow.set(['', '', '', '']);
          this.selectedIndex.set(0);
        }
      },
      error: (err) => {
        console.error('🔥 erro ao enviar tentativa:', err);
      }
    });
  }

  clearCell() {
    if (this.gameStatus() !== 'IN_PROGRESS') return;

    const index = this.selectedIndex();
    if (index === null) return;

    const updated = [...this.currentRow()];
    updated[index] = '';
    this.currentRow.set(updated);
  }

  clearRow() {
    if (this.gameStatus() !== 'IN_PROGRESS') return;

    this.currentRow.set(['', '', '', '']);
    this.selectedIndex.set(0);
  }

  goBack() {
    this.router.navigate(['/dashboard']);
  }
}