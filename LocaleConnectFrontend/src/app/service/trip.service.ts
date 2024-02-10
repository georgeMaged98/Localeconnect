// trip.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {Trip} from "../model/trip";
import {Itinerary} from "../model/itinerary";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class TripService {
  private apiUrl =`${environment.API_URL}/api/trip`;
  private tripSource = new BehaviorSubject<Trip | null>(null);
  currentTrip = this.tripSource.asObservable();

  constructor(private http: HttpClient) { }
  changeTrip(trip: any) {
    if(trip){
      this.tripSource.next(trip);

    }
  }
  createTrip(trip: Trip): Observable<Trip> {
    return this.http.post<Trip>(`${this.apiUrl}/create`, trip);
  }

  getAllTrips(): Observable<Trip[]> {
    return this.http.get<Trip[]>(`${this.apiUrl}/all`);
  }

  getTripById(tripId: number): Observable<Trip> {
    return this.http.get<Trip>(`${this.apiUrl}/${tripId}`);
  }

  updateTrip(tripId: number, trip: Trip): Observable<Trip> {
    return this.http.put<Trip>(`${this.apiUrl}/update/${tripId}`, trip);
  }

  deleteTrip(tripId: number): Observable<{}> {
    return this.http.delete(`${this.apiUrl}/delete/${tripId}`);
  }
  joinTrip(id: number, travellerId: number) {
    return this.http.post(`${this.apiUrl}/${id}/join`, {travellerId});
  }

  leaveTrip(id: number, travellerId: number) {
    return this.http.post(`${this.apiUrl}/${id}/leave`, {travellerId});
  }

  searchTrip(tripName: string): Observable<Trip[]> {
    return this.http.get<Trip[]>(`${this.apiUrl}/search?name=${tripName}`);
  }

}
