import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog} from '@angular/material/dialog';

type DialogData = {
  title: string;
  error: string;
}

@Component({
  selector: 'app-error-popup',
  templateUrl: './error-popup.component.html',
  styleUrls: ['./error-popup.component.scss']
})
export class ErrorPopupComponent {
  data?: DialogData;

  constructor(@Inject(MAT_DIALOG_DATA) data: DialogData, public dialog: MatDialog) {
    this.data = {...data}
  }

  close() {
    this.dialog.closeAll();
  }
}
