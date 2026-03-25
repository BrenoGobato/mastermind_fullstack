import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import {
  provideHttpClientTesting,
  HttpTestingController
} from '@angular/common/http/testing';

import { MatchService } from './match';
import { Match, AttemptResponse } from '../../shared/models/match-model';

describe('MatchService', () => {
  let service: MatchService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        MatchService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });

    service = TestBed.inject(MatchService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call createMatch endpoint', () => {
    const response: Match = {
      id: 1,
      matchStatus: 'IN_PROGRESS',
      initialDate: '2026-03-25T21:00:00',
      finalDate: null,
      attempts: [],
      correctAnswer: null
    };

    service.createMatch('1').subscribe((result) => {
      expect(result).toEqual(response);
    });

    const req = httpMock.expectOne('http://localhost:8080/matches');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ userId: '1' });

    req.flush(response);
  });

  it('should call getMatchById endpoint', () => {
    const response: Match = {
      id: 1,
      matchStatus: 'IN_PROGRESS',
      initialDate: '2026-03-25T21:00:00',
      finalDate: null,
      attempts: [],
      correctAnswer: null
    };

    service.getMatchById(1).subscribe((result) => {
      expect(result).toEqual(response);
    });

    const req = httpMock.expectOne('http://localhost:8080/matches/1');
    expect(req.request.method).toBe('GET');

    req.flush(response);
  });

  it('should call submitAttempt endpoint', () => {
    const payload = {
      sequence: ['RED', 'BLUE', 'YELLOW', 'WHITE']
    };

    const response: AttemptResponse = {
      correctPositions: 2,
      matchStatus: 'IN_PROGRESS',
      attemptsLeft: 9,
      correctAnswer: null
    };

    service.submitAttempt(1, payload).subscribe((result) => {
      expect(result).toEqual(response);
    });

    const req = httpMock.expectOne('http://localhost:8080/matches/1/attempts');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);

    req.flush(response);
  });

  it('should call getInProgressMatches endpoint', () => {
    const response: Match[] = [
      {
        id: 1,
        matchStatus: 'IN_PROGRESS',
        initialDate: '2026-03-25T21:00:00',
        finalDate: null,
        attempts: [],
        correctAnswer: null
      }
    ];

    service.getInProgressMatches('1').subscribe((result) => {
      expect(result).toEqual(response);
    });

    const req = httpMock.expectOne(
      'http://localhost:8080/matches?status=IN_PROGRESS&userId=1'
    );
    expect(req.request.method).toBe('GET');

    req.flush(response);
  });
});