import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = '/api/user';
  constructor(private http: HttpClient) { }
//TODO: add api endpoint to get user by id
  getUsername(userId: number): Observable<string> {
    return this.http.get<string>(`${this.apiUrl}/${userId}/username`);
  }
}
