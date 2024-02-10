import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {GuideProfile} from "../model/guide";
import {TripPreview} from "../model/trip";
import {User} from "../model/user";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = '/api/user';

  constructor(private http: HttpClient) {
  }

//TODO: add api endpoint to get user by id
  getUsername(userId: number): Observable<string> {
    return this.http.get<string>(`${this.apiUrl}/secured/${userId}`);
  }

  searchGuides(searchTerm: string, searchGuides: GuideProfile[]): GuideProfile[] {
    if (!searchTerm) {
      return [...searchGuides];
    }
    return searchGuides.filter(guide =>
      guide.name.toLowerCase().includes(searchTerm.toLowerCase())
    );
  }


  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/all`);
  }

  getUserById(userId: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${userId}`);
  }

  getGuidesMock(): GuideProfile[] {
    return [
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
        imageUrl: 'https://www.profilebakery.com/wp-content/uploads/2023/04/AI-Profile-Picture.jpg',
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
        trips: this.MOCK_TRIP_PREVIEWS
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

  MOCK_TRIP_PREVIEWS: TripPreview[] = [
    {
      id: 1,
      name: 'Explore the Mountains',
      description: 'Join us for an unforgettable adventure in the mountains. Experience breathtaking views, challenging hikes, and the serenity of nature.',
      link: '/trips/mountains' // Adjust the link as necessary for your routing
    },
    {
      id: 2,
      name: 'City Lights Tour',
      description: 'Discover the vibrant life of the city after dark. This tour takes you through bustling streets, markets, and landmarks illuminated beautifully at night.',
      link: '/trips/city-lights'
    },
    {
      id: 3,
      name: 'Safari Adventure',
      description: 'Get close to nature and wildlife on this exciting safari adventure. See wild animals in their natural habitat and learn about conservation efforts.',
      link: '/trips/safari'
    },
    {
      id: 4,
      name: 'Cultural Heritage Tour',
      description: 'Explore the rich cultural heritage of the region. Visit historical sites, museums, and monuments that tell the story of the people and their traditions.',
      link: '/trips/cultural-heritage'
    }
  ];
}
