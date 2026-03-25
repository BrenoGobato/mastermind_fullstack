import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import {
  provideHttpClientTesting,
  HttpTestingController
} from '@angular/common/http/testing';

import { RankingService } from './ranking';

describe('RankingService', () => {
  let service: RankingService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        RankingService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });

    service = TestBed.inject(RankingService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call getRanking endpoint', () => {
    const response = [
      {
        username: 'breno',
        totalAttempts: 1,
        durationInSeconds: 120
      },
      {
        username: 'ana',
        totalAttempts: 2,
        durationInSeconds: 200
      }
    ];

    service.getRanking().subscribe((result) => {
      expect(result).toEqual(response);
    });

    const req = httpMock.expectOne('http://localhost:8080/ranking');
    expect(req.request.method).toBe('GET');

    req.flush(response);
  });
});