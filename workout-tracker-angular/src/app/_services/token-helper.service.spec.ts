/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { TokenHelperService } from './token-helper.service';

describe('Service: TokenHelper', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [TokenHelperService]
    });
  });

  it('should ...', inject([TokenHelperService], (service: TokenHelperService) => {
    expect(service).toBeTruthy();
  }));
});
