// itinerary.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {Itinerary, Tag} from "../model/itinerary";

@Injectable({
  providedIn: 'root'
})
export class ItineraryService {
  private itinerarySource = new BehaviorSubject<Itinerary | null>(null);
  currentItinerary = this.itinerarySource.asObservable();


  //TODO: CONFIGURE PROXY
  private apiUrl = '/api/itinerary';

  constructor(private http: HttpClient) {}
  changeItinerary(itinerary: any) {
    if(itinerary){
      this.itinerarySource.next(itinerary);

    }
  }
  //TODO: replace mock with api call once docker is set
  getItineraries(): Observable<Itinerary[]> {
    return this.http.get<Itinerary[]>(`${this.apiUrl}/all`);
  }
  getItinerariesMock():Itinerary[]{
    const itinerary1: Itinerary = {
      id: 1,
      userId: 100,
      username:'',
      name: "Explore the Alps",
      description: "A thrilling adventure in the Alps exploring nature and enjoying winter sports.",
      numberOfDays: 7,
      tags: this.mapTags([Tag.ADVENTURE, Tag.WINTER_SPORTS, Tag.NATURE]),
      placesToVisit: ["Mount Blanc", "Zermatt", "Jungfrau Region"],
      dailyActivities: ["Skiing in Zermatt", "Hiking in Jungfrau", "Visit to Ice Caves"],
      imageUrls: ["assets/pictures/background-landing-page.png"], expand:false,
      rating:0
    };

    const itinerary2: Itinerary = {
      id: 2,
      userId: 101,
      username:'',
      name: "Cultural Tour of Japan",
      description: "Immersive experience into the rich culture and history of Japan.",
      numberOfDays: 10,
      tags: this.mapTags([Tag.CULTURAL, Tag.FOODIE, Tag.HISTORICAL]),
      placesToVisit: ["Kyoto", "Tokyo", "Osaka"],
      dailyActivities: ["Visit Kinkaku-ji", "Tour of Akihabara", "Osaka street food tour"],
      imageUrls:[],expand:false,
      rating:0
    };
    return [itinerary1,itinerary2];
  }

  addItinerary(itinerary: Itinerary): Observable<Itinerary> {
    return this.http.post<Itinerary>(`${this.apiUrl}/create`, itinerary);
  }
  addItineraryMock(itinerary: Itinerary): Itinerary{
    return itinerary;
  }
mapTags(tags: Tag[]): string[]{
    return  tags.map(tag =>Tag[tag]);
}

}
