import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-file-input',
  templateUrl: './file-input.component.html',
  styleUrls: ['./file-input.component.scss']
})
export class FileInputComponent implements OnInit {
  srcResult: string | ArrayBuffer | null | undefined;

  constructor() { }

  ngOnInit(): void {
  }

  onFileSelected(): void {
    const inputNode = <HTMLInputElement> document.getElementById('file');
    const reader = new FileReader();
    reader.onload = (e) => {
      this.srcResult = e.target?.result;
      if(inputNode.files) reader.readAsArrayBuffer(inputNode.files[0]);
    }
  }
}
