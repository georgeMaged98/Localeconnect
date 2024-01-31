import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {Review} from "../model/review";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  constructor(private http: HttpClient) {}

  createReview(review: Review): Observable<Review> {
    return this.http.post<Review>('/api/create-review', review);
  }

  updateReview(id: number, review: Review): Observable<Review> {
    return this.http.put<Review>(`/api/update-review/${id}`, review);
  }

  deleteReview(id: number): Observable<void> {
    return this.http.delete<void>(`/api/delete-review/${id}`);
  }

  getAllReviews(itineraryId: number): Observable<Review[]> {
    return this.http.get<Review[]>(`/api/all-reviews/${itineraryId}`);
  }
}



