import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ErrorPopupComponent } from './error-popup.component';
import {MAT_DIALOG_DATA, MatDialog} from "@angular/material/dialog";

describe('ErrorPopupComponent', () => {
  let component: ErrorPopupComponent;
  let fixture: ComponentFixture<ErrorPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ErrorPopupComponent ],
      providers: [{ provide: MAT_DIALOG_DATA, useValue: {} }, { provide: MatDialog, useValue: {} }],
    })
    .compileComponents();

    fixture = TestBed.createComponent(ErrorPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
