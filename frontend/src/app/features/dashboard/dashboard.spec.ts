import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, provideRouter, Router } from '@angular/router';
import { of } from 'rxjs';

import { Dashboard } from './dashboard';
import { AuthService } from '../../core/services/auth';
import { MatchService } from '../../core/services/match';

describe('Dashboard', () => {
  let component: Dashboard;
  let fixture: ComponentFixture<Dashboard>;

  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let matchServiceSpy: jasmine.SpyObj<MatchService>;
  let router: Router;

  beforeEach(async () => {
    authServiceSpy = jasmine.createSpyObj<AuthService>(
      'AuthService',
      ['user', 'logout']
    );

    matchServiceSpy = jasmine.createSpyObj<MatchService>(
      'MatchService',
      ['createMatch', 'getInProgressMatches']
    );

    authServiceSpy.user.and.returnValue({
      id: '1',
      username: 'breno',
      email: 'breno@email.com'
    });

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

    matchServiceSpy.getInProgressMatches.and.returnValue(of([]));

    await TestBed.configureTestingModule({
      imports: [Dashboard],
      providers: [
        provideRouter([]),
        { provide: AuthService, useValue: authServiceSpy },
        { provide: MatchService, useValue: matchServiceSpy },
        { provide: ActivatedRoute, useValue: {} }
      ]
    }).compileComponents();

    router = TestBed.inject(Router);
    spyOn(router, 'navigate').and.returnValue(Promise.resolve(true));

    fixture = TestBed.createComponent(Dashboard);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});