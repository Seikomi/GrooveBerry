import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReadingQueueComponent } from './reading-queue.component';

describe('ReadingQueueComponent', () => {
  let component: ReadingQueueComponent;
  let fixture: ComponentFixture<ReadingQueueComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReadingQueueComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReadingQueueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
