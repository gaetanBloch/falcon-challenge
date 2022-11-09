import { TestBed } from '@angular/core/testing';

import { UploadService } from './upload-service';
import { HttpClient } from "@angular/common/http";

describe('UploadService', () => {
  let service: UploadService;

  beforeEach(() => {
    TestBed.configureTestingModule({providers: [{provide: HttpClient, useValue: {}}]});
    service = TestBed.inject(UploadService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
