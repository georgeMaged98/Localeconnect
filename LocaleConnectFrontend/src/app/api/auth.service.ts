import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";
import {User} from "../model/user";
import {Traveler} from "../model/traveler";
import {Guide} from "../model/guide";

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

  registerTraveler(traveler:Traveler): Observable<Traveler> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json'});
    return this.http.post<Traveler>(`${this.apiUrl}/register`, traveler, { headers })
      .pipe(
      catchError(this.handleError<Traveler>('register traveler'))
    );
  }
  registerGuide(guide:Guide): Observable<Guide> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<Guide>(`${this.apiUrl}/register`, guide, { headers })
      .pipe(
        catchError(this.handleError<Guide>('register guide'))
      );
  }
  //TODO: handle login when the backend method is available
  login(credentials: { username: string, password: string }): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post(`${this.apiUrl}/login`, credentials, { headers }).pipe(
      catchError(this.handleError('login user'))
    );
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
          }
          else if (error.error.includes('username does not exist')) {
            errorMessage = 'This username doesnt exist.';
          }
          else if (error.error.includes('wrong password')) {
            errorMessage = 'This password is wrong.';
          }else {
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
