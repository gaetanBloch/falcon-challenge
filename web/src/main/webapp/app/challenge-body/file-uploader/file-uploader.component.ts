import { Component, ElementRef, EventEmitter, Input, Output, ViewChild } from '@angular/core';

@Component({
  selector: 'app-file-uploader',
  templateUrl: './file-uploader.component.html',
  styleUrls: ['./file-uploader.component.scss']
})
export class FileUploaderComponent {
  // Make it optional because of TS strict initilization
  @ViewChild('fileUpload', { static: false }) fileUpload?: ElementRef;
  @Input() title = '';
  @Input() multiple = false;
  @Output() filesUploaded = new EventEmitter<File[]>()
  files: File[] = [];
  fileNames: string[] = [];

  onClick(): void {
    const fileUpload: HTMLInputElement = this.fileUpload?.nativeElement;
    fileUpload.onchange = () => {
      if (fileUpload.files === null) return;
      for (let index = 0; index < fileUpload.files.length; index++) {
        const file = fileUpload.files[index];
        this.files.push(file);
      }
      this.emitFiles();
    };
    // Clear the file input cache
    fileUpload.click();
  }

  private emitFiles(): void {
    if (!this.fileUpload) return;
    this.fileUpload.nativeElement.value = '';
    this.filesUploaded.emit(this.files);
    // Empty array
    this.files.splice(0, this.files.length);
  }
}

