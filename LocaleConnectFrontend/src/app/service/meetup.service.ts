import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Meetup} from "../model/meetup";
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class MeetupService {
  private apiUrl = '/api/meetup';
  private meetupSource = new BehaviorSubject<Meetup | null>(null);
  currentMeetup = this.meetupSource.asObservable();

  constructor(private http: HttpClient) {
  }

  changeMeetup(meetup: any) {
    if (meetup) {
      this.meetupSource.next(meetup);

    }
  }

  getAllMeetups() {
    return this.http.get<Meetup[]>(`${this.apiUrl}/`);
  }

  getMeetupById(id: number) {
    return this.http.get<Meetup>(`${this.apiUrl}/${id}`);
  }

  createMeetup(meetup: Meetup) {
    return this.http.post<Meetup>(`${this.apiUrl}/`, meetup);
  }

  updateMeetup(id: number, meetup: Meetup) {
    return this.http.put<Meetup>(`${this.apiUrl}/${id}`, meetup);
  }

  attendMeetup(id: number, travellerId: number) {
    return this.http.post(`${this.apiUrl}/${id}/attend`, {travellerId});
  }

  unattendMeetup(id: number, travellerId: number) {
    return this.http.post(`${this.apiUrl}/${id}/unattend`, {travellerId});
  }

  deleteMeetup(id: number) {
    return this.http.delete<Meetup>(`${this.apiUrl}/${id}`);
  }


  searchMeetups(searchTerm: string, searchMeetups: Meetup[]): Meetup[] {
    if (!searchTerm) {
      return [...searchMeetups];
    }
    return searchMeetups.filter(meetup =>
      meetup.name.toLowerCase().includes(searchTerm.toLowerCase())
    );
  }

  getMeetupsMocks(): Meetup[] {
    return [
      {
        id: 1,
        creatorName: 'Yassin M.',
        name: 'Mountain Hikers Club',
        description: 'An exhilarating hike through the scenic trails of the Green Mountains.',
        date: new Date(2024, 7, 10),
        startTime: '08:00 AM',
        endTime: '02:00 PM',
        cost: 0,
        location: 'Green Mountains, VT',
        spokenLanguages: ['English'],
        meetupAttendees: [2001, 2002, 2003],
        rating: 0,
      },
      {
        id: 2,
        creatorName: 'Ashley J.',
        name: 'Sunset Photography',
        description: 'Capture the stunning sunset with fellow photography enthusiasts.',
        date: new Date(2024, 7, 12),
        startTime: '06:00 PM',
        endTime: '08:00 PM',
        cost: 20.00,
        location: 'Ocean View Beach, CA',
        spokenLanguages: ['English', 'Spanish'],
        meetupAttendees: [2004, 2005], rating: 0, averageRating: 2.5, totalRatings: 5
      },
      {
        id: 3,
        creatorName: 'Phillip K.',
        name: 'Urban Sketchers Meetup',
        description: 'Join us to sketch our city\'s beautiful historic buildings.',
        date: new Date(2024, 7, 15),
        startTime: '10:00 AM',
        endTime: '01:00 PM',
        cost: 0,
        location: 'Downtown Central Plaza, NY',
        spokenLanguages: ['English', 'French'],
        meetupAttendees: [], averageRating: 4,
        rating: 0, totalRatings: 1
      }
    ];
  }
}
