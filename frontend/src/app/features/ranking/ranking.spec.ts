import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';

import { Ranking } from './ranking';
import { RankingService } from '../../core/services/ranking';

describe('Ranking', () => {
  let component: Ranking;
  let fixture: ComponentFixture<Ranking>;

  let rankingServiceSpy: jasmine.SpyObj<RankingService>;

  beforeEach(async () => {
    rankingServiceSpy = jasmine.createSpyObj<RankingService>(
      'RankingService',
      ['getRanking']
    );

    rankingServiceSpy.getRanking.and.returnValue(
      of([
        {
          username: 'breno',
          totalAttempts: 1,
          durationInSeconds: 120
        }
      ])
    );

    await TestBed.configureTestingModule({
      imports: [Ranking],
      providers: [
        { provide: RankingService, useValue: rankingServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Ranking);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load ranking on init', () => {
    expect(rankingServiceSpy.getRanking).toHaveBeenCalled();
  });
});