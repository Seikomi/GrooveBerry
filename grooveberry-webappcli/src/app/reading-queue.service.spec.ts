import { TestBed, inject } from '@angular/core/testing';

import { ReadingQueueService } from './reading-queue.service';

describe('ReadingQueueService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ReadingQueueService]
    });
  });

  it('should be created', inject([ReadingQueueService], (service: ReadingQueueService) => {
    expect(service).toBeTruthy();
  }));
});
