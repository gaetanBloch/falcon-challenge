import {Injectable} from '@angular/core';
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import {ErrorPopupComponent} from "../error-popup/error-popup.component";

@Injectable({
  providedIn: 'root'
})
export class DialogService {

  constructor(private dialog: MatDialog) {
  }

  openErrorPopup(title: string, errorMessage: string) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.autoFocus = true;
    dialogConfig.closeOnNavigation = true;
    dialogConfig.data = {
      title,
      error: errorMessage
    }
    this.dialog.open(ErrorPopupComponent, dialogConfig);
  }
}
