import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { ApiResponse } from '../model/apiResponse';

@Injectable({
  providedIn: 'root',
})
export class ImagesService {
  private imagesSource = new BehaviorSubject<string[]>([]);
  currentImages = this.imagesSource.asObservable();
  private apiUrl = 'http://localhost:8080/api/gcp';

  constructor(private http: HttpClient) {}

  updateImages(images: string[]) {
    this.imagesSource.next(images);
  }

  getImage(filename: string): Observable<ApiResponse> {
    return this.http.get<ApiResponse>(`${this.apiUrl}/?filename=${filename}`);
  }
}
