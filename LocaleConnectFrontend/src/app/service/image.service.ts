import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ImagesService {
  private imagesSource = new BehaviorSubject<string[]>([]);
  currentImages = this.imagesSource.asObservable();

  constructor() { }

  updateImages(images: string[]) {
    this.imagesSource.next(images);
  }
}
