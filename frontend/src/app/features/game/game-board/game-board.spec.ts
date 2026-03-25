import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, provideRouter, Router } from '@angular/router';
import { of } from 'rxjs';

import { GameBoard } from './game-board';
import { MatchService } from '../../../core/services/match';
import { AuthService } from '../../../core/services/auth';

describe('GameBoard', () => {
  let component: GameBoard;
  let fixture: ComponentFixture<GameBoard>;

  let matchServiceSpy: jasmine.SpyObj<MatchService>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let router: Router;

  beforeEach(async () => {
    matchServiceSpy = jasmine.createSpyObj<MatchService>(
      'MatchService',
      ['getMatchById', 'submitAttempt', 'createMatch']
    );

    authServiceSpy = jasmine.createSpyObj<AuthService>(
      'AuthService',
      ['user']
    );

    authServiceSpy.user.and.returnValue({
      id: '1',
      username: 'breno',
      email: 'breno@email.com'
    });

    matchServiceSpy.getMatchById.and.returnValue(
      of({
        id: 1,
        matchStatus: 'IN_PROGRESS',
        initialDate: '2026-03-25T21:00:00',
        finalDate: null,
        attempts: [],
        correctAnswer: null
      })
    );

    matchServiceSpy.submitAttempt.and.returnValue(
      of({
        correctPositions: 2,
        matchStatus: 'IN_PROGRESS',
        attemptsLeft: 9,
        correctAnswer: null
      })
    );

    matchServiceSpy.createMatch.and.returnValue(
      of({
        id: 1,
        matchStatus: 'IN_PROGRESS',
        initialDate: '2026-03-25T21:00:00',
        finalDate: null,
        attempts: [],
        correctAnswer: null
      })
    );

    await TestBed.configureTestingModule({
      imports: [GameBoard],
      providers: [
        provideRouter([]),
        { provide: MatchService, useValue: matchServiceSpy },
        { provide: AuthService, useValue: authServiceSpy },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: {
                get: (key: string) => (key === 'id' ? '1' : null)
              }
            },
            params: of({ id: '1' })
          }
        }
      ]
    }).compileComponents();

    router = TestBed.inject(Router);
    spyOn(router, 'navigate').and.returnValue(Promise.resolve(true));

    fixture = TestBed.createComponent(GameBoard);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});