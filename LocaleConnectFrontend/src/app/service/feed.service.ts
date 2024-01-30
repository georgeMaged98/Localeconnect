import { Injectable } from '@angular/core';
import {Post} from "../model/feed";
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class FeedService {
  private apiUrl = '/api/feed';

  constructor(private http: HttpClient) { }

  getPosts(): Observable<Post[]> {
    return this.http.get<Post[]>(`${this.apiUrl}/all`);
  }
}

