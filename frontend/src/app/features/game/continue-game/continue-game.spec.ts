import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ContinueGame } from './continue-game';

describe('ContinueGame', () => {
  let component: ContinueGame;
  let fixture: ComponentFixture<ContinueGame>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ContinueGame]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ContinueGame);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
