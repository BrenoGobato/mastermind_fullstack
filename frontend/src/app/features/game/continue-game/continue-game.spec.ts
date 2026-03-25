import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';

import { ContinueGame } from './continue-game';
import { MatchService } from '../../../core/services/match';
import { AuthService } from '../../../core/services/auth';

describe('ContinueGame', () => {
  let component: ContinueGame;
  let fixture: ComponentFixture<ContinueGame>;

  let matchServiceSpy: jasmine.SpyObj<MatchService>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;

  beforeEach(async () => {
    matchServiceSpy = jasmine.createSpyObj<MatchService>(
      'MatchService',
      ['getInProgressMatches']
    );

    authServiceSpy = jasmine.createSpyObj<AuthService>(
      'AuthService',
      ['user']
    );

    // mock do usuário logado (IMPORTANTÍSSIMO)
    authServiceSpy.user.and.returnValue({
      id: '1',
      username: 'breno',
      email: 'breno@email.com'
    });

    matchServiceSpy.getInProgressMatches.and.returnValue(of([]));

    await TestBed.configureTestingModule({
      imports: [ContinueGame],
      providers: [
        provideRouter([]),
        { provide: MatchService, useValue: matchServiceSpy },
        { provide: AuthService, useValue: authServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ContinueGame);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load matches on init', () => {
    expect(matchServiceSpy.getInProgressMatches).toHaveBeenCalledWith('1');
  });
});