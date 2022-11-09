import {Component} from '@angular/core';
import {catchError} from "rxjs/operators";
import {HttpErrorResponse} from "@angular/common/http";
import {of} from "rxjs";
import {UploadService} from "../services/upload-service";
import {DialogService} from "../services/dialog.service";

type EmpireData = {
  countdown: number;
  bounty_hunters: [];
}

@Component({
  selector: 'app-challenge-body',
  templateUrl: './challenge-body.component.html',
  styleUrls: ['./challenge-body.component.scss']
})
export class ChallengeBodyComponent {
  empireFile?: File;
  countdown: number = 0
  odds?: number;

  constructor(private uploadService: UploadService, private dialogService: DialogService) {
  }

  onFileUploaded(file: File[]) {
    // Reset odds
    this.odds = undefined
    this.empireFile = file[0];
    let fileReader = new FileReader();
    let self = this;
    fileReader.onloadend = () => {
      if (fileReader.result) {
        let content = fileReader.result.toString();
        try {
          let empire: EmpireData = JSON.parse(content);
          self.validateEmpire(empire);
          self.countdown = empire.countdown;
        } catch (error: any) {
          // Reset countdown
          self.countdown = 0
          self.openInvalidEmpireFilePopup(error);
        }
      }
    };
    //@ts-ignore
    fileReader.readAsText(this.empireFile);
  }

  private validateEmpire(empire: EmpireData) {
    if (!empire.countdown) {
      throw new Error("Countdown is missing");
    }
    if (empire.countdown < 1) {
      throw new Error("Countdown is <= 0");
    }
    if (!empire.bounty_hunters) {
      throw new Error("Bounty hunters are missing");
    }
  }

  private openInvalidEmpireFilePopup(error: Error) {
    this.dialogService.openErrorPopup("Error: Invalid File", "The file you " +
      "uploaded is not a valid Empire file, error: " + error.message);
  }

  onUpload(): void {
    const formData = new FormData();
    // @ts-ignore
    formData.append('file', this.empireFile);
    this.uploadService.upload(formData).pipe(
      catchError((error: HttpErrorResponse) => {
        return of(`Error ${error.status} ${error.error.status}: ${error.error.message}`);
      }))
    .subscribe((event: any) => {
      if (typeof (event) === 'number') {
        this.odds = event;
      } else if (typeof (event) === 'string') {
        this.dialogService.openErrorPopup("Error: Upload Failed", event);
      } else {
        this.dialogService.openErrorPopup("Error: Upload Failed", `Error ${event.status} Unexpected error`);
      }
    });
  }
}
