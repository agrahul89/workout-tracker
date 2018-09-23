/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { DateTimeService } from './date-time.service';

describe('Service: DateTime', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DateTimeService]
    });
  });

  it('should ...', inject([DateTimeService], (service: DateTimeService) => {
    expect(service).toBeTruthy();
  }));
});
