import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LeagueBarChartComponent } from './league-bar-chart.component';

describe('LeagueBarChartComponent', () => {
  let component: LeagueBarChartComponent;
  let fixture: ComponentFixture<LeagueBarChartComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LeagueBarChartComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LeagueBarChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
