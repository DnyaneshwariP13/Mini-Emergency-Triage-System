import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Triageform } from './triageform';

describe('Triageform', () => {
  let component: Triageform;
  let fixture: ComponentFixture<Triageform>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Triageform],
    }).compileComponents();

    fixture = TestBed.createComponent(Triageform);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
