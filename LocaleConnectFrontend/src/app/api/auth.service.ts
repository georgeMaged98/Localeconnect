import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";
import {User} from "../model/user";
import {Traveler} from "../model/traveler";

@Injectable({
  providedIn: 'root'
})

export class AuthService {
  private apiUrl = '/api/user';

  constructor(private http: HttpClient) {}

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/all`);
  }

  getUserById(userId: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${userId}`);
  }

  registerTraveler(user:User): Observable<User> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json'});
    return this.http.post<User>(`${this.apiUrl}/register`, user, { headers })
      .pipe(
      catchError(this.handleError<User>('register user'))
    );
  }
  registerGuide(traveler:Traveler): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<User>(`${this.apiUrl}/register`, traveler, { headers });
  }
  login(credentials: { username: string, password: string }): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post(`${this.apiUrl}/login`, credentials, { headers });
  }
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: HttpErrorResponse): Observable<T> => {
      let errorMessage = '';

      if (error.error instanceof ErrorEvent) {
        // Client-side error
        errorMessage = error.error.message;
      } else {
        // Backend error
        if (error.status === 400) {
          if (error.error.includes('username already exists')) {
            errorMessage = 'This username is already taken. Please try a different one.';
          } else if (error.error.includes('email already exists')) {
            errorMessage = 'This email is already in use. Please try a different one.';
          } else {
            errorMessage = 'Validation error. Please check your input.';
          }
        } else if (error.status >= 500) {
          errorMessage = 'Server error. Please try again later.';
        } else {
          errorMessage = 'An unexpected error occurred. Please try again.';
        }
      }
      return throwError(errorMessage);
    };
  }

}
