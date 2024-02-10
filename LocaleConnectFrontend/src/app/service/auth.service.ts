import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from "@angular/common/http";
import {BehaviorSubject, catchError, map, Observable, tap, throwError} from "rxjs";
import {User} from "../model/user";
import {Traveler} from "../model/traveler";
import {Guide} from "../model/guide";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})

export class AuthService {
  private apiUrl = `${environment.API_URL}/api/user/auth`;
  private currentUserSubject: BehaviorSubject<User | null>;
  public currentUser: Observable<User | null>;

  constructor(private http: HttpClient) {
    this.currentUserSubject = new BehaviorSubject<User | null>(this.getUserFromLocalStorage());
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): User | null {
    return this.currentUserSubject.value;
  }
  login(email: string, password: string): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/login`, {email, password}).pipe(
      tap(user => this.setSession(user)),
      map(user => {
        this.currentUserSubject.next(user);
        return user;
      }),
      catchError(this.handleError<User>('login'))
    );
  }


  logout(): void {
    localStorage.removeItem('currentUser');
    localStorage.removeItem('expires_at');
    this.currentUserSubject.next(null);
  }

  public isLoggedIn(): boolean {
    return new Date().getTime() < parseInt(localStorage.getItem('expires_at') || '0');
  }

  public isLoggedOut(): boolean {
    return !this.isLoggedIn();
  }
  registerTraveler(traveler: Traveler): Observable<Traveler> {
    const headers = new HttpHeaders({'Content-Type': 'application/json'});
    return this.http.post<Traveler>(`${this.apiUrl}/register-traveler`, traveler, {headers})
      .pipe(
        catchError(this.handleError<Traveler>('register traveler'))
      );
  }

  registerGuide(guide: Guide): Observable<Guide> {
    const headers = new HttpHeaders({'Content-Type': 'application/json'});
    return this.http.post<Guide>(`${this.apiUrl}/register-localguide`, guide, {headers})
      .pipe(
        catchError(this.handleError<Guide>('register guide'))
      );
  }

  private getUserFromLocalStorage(): User | null {
    const storedUser = localStorage.getItem('currentUser');
    if (!storedUser) return null;
    return JSON.parse(storedUser);
  }

  private setSession(authResult: User): void {
    localStorage.setItem('currentUser', JSON.stringify(authResult));
    //TODO: Set the time that the access token will expire at
    const expiresAt = JSON.stringify((1000 * 60 * 30) + new Date().getTime());
    localStorage.setItem('expires_at', expiresAt);
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
          } else if (error.error.includes('username does not exist')) {
            errorMessage = 'This username doesnt exist.';
          } else if (error.error.includes('wrong password')) {
            errorMessage = 'This password is wrong.';
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
