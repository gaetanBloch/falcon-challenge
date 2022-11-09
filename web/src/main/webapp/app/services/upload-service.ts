import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class UploadService {
  private readonly endpoint: string;

  constructor(private httpClient: HttpClient) {
    this.endpoint = `${location.protocol}//${location.host}${environment.oddsApiPath}`;
  }

  public upload(formData: FormData): Observable<HttpEvent<any>> {
    return this.httpClient.post<any>(this.endpoint, formData, {
      responseType: 'json',
    });
  }
}
