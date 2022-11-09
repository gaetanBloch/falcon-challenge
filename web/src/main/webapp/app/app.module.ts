import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ChallengeBodyComponent } from './challenge-body/challenge.body.component';
import { ReactiveFormsModule } from "@angular/forms";
import { HttpClientModule } from "@angular/common/http";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from "./material-module/material.module";
import { FileInputComponent } from './challenge-body/file-input/file-input.component';
import { FileUploaderComponent } from './challenge-body/file-uploader/file-uploader.component';
import { ErrorPopupComponent } from './error-popup/error-popup.component';

@NgModule({
  declarations: [
    AppComponent,
    ChallengeBodyComponent,
    FileUploaderComponent,
    FileInputComponent,
    ErrorPopupComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MaterialModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
  entryComponents: [ErrorPopupComponent]
})
export class AppModule { }
