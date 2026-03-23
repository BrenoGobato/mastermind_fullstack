import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  { path: 'login', loadComponent: () => import('./features/auth/login/login').then(m => m.Login) },
  { path: 'register', loadComponent: () => import('./features/auth/register/register').then(m => m.Register) },

  { path: 'dashboard', loadComponent: () => import('./features/dashboard/dashboard').then(m => m.Dashboard) },

  { path: 'game/new', loadComponent: () => import('./features/game/game-board/game-board').then(m => m.GameBoard) },
  { path: 'game/:id', loadComponent: () => import('./features/game/game-board/game-board').then(m => m.GameBoard) },

  { path: 'continue', loadComponent: () => import('./features/game/continue-game/continue-game').then(m => m.ContinueGame) },

  { path: 'ranking', loadComponent: () => import('./features/ranking/ranking').then(m => m.Ranking) },
];
