import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Meetup} from "../model/meetup";
import {GuideProfile} from "../model/guide";

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
  searchGuides(searchTerm: string, searchGuides: GuideProfile[]): GuideProfile[] {
    if (!searchTerm) {
      return [...searchGuides];
    }
    return searchGuides.filter(guide =>
      guide.name.toLowerCase().includes(searchTerm.toLowerCase())
    );
  }
  getGuidesMock(): GuideProfile[]{
    return  [
      {
        id: 1,
        name: 'John Doe',
        userName: 'john_doe',
        bio: 'Passionate traveler and language enthusiast.',
        visitedCountries: ['USA', 'Canada', 'France', 'Italy'],
        languages: ['English', 'French', 'Italian'],
        city: 'New York',
        rating: 4.8,
        ratingSubmitted: true,
        totalRatings: 50,
        averageRating: 4.8,
        expand: false
      },
      {
        id: 2,
        name: 'Emily Smith',
        userName: 'emily_smith',
        bio: 'Experienced guide with a love for history.',
        visitedCountries: ['UK', 'Germany', 'Spain', 'Greece'],
        languages: ['English', 'German', 'Spanish'],
        city: 'London',
        rating: 0,
        totalRatings: 35,
        averageRating: 2.5,
      },
      {
        id: 3,
        name: 'Carlos Rodriguez',
        userName: 'carlos_rodriguez',
        bio: 'Adventure seeker and nature lover.',
        visitedCountries: ['Mexico', 'Costa Rica', 'Brazil', 'Peru'],
        languages: ['Spanish', 'Portuguese', 'English'],
        city: 'Mexico City',
        rating: 0,
        totalRatings: 65,
        averageRating: 4.9,
      },
      {
        id: 4,
        name: 'Anna Chen',
        userName: 'anna_chen',
        bio: 'Food enthusiast and culinary expert.',
        visitedCountries: ['China', 'Japan', 'Thailand', 'Vietnam'],
        languages: ['Mandarin', 'Japanese', 'English'],
        city: 'Beijing',
        rating: 0,
        totalRatings: 45,
        averageRating: 4.7,
      },
      {
        id: 5,
        name: 'Ahmed Khalid',
        userName: 'ahmed_khalid',
        bio: 'Passionate about showcasing cultural diversity.',
        visitedCountries: ['Egypt', 'Turkey', 'Morocco', 'UAE'],
        languages: ['Arabic', 'Turkish', 'English'],
        city: 'Cairo',
        rating: 4.6,
        ratingSubmitted: true,
        totalRatings: 40,
        averageRating: 4.6,
      }
    ];
  }
}
