import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { map, Observable, throwError } from 'rxjs';
import { Guide, GuideProfile } from '../model/guide';
import { TripPreview } from '../model/trip';
import { User } from '../model/user';
import { environment } from '../../environments/environment';
import { AuthService } from './auth.service';
import { ApiResponse } from '../model/apiResponse';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiUrl = `${environment.API_URL}/api/user/secured`;
  headers: HttpHeaders;

  constructor(private http: HttpClient, private authService: AuthService) {
    this.headers = this.authService.getHttpHeaders();
  }

  getUsername(userId: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${userId}`);
  }

  searchGuides(
    searchTerm: string,
    searchGuides: GuideProfile[]
  ): GuideProfile[] {
    if (!searchTerm) {
      return [...searchGuides];
    }
    return searchGuides.filter((guide) =>
      guide.name.toLowerCase().includes(searchTerm.toLowerCase())
    );
  }

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/all`);
  }

  getUserById(userId: number): Observable<ApiResponse> {
    return this.http.get<ApiResponse>(`${this.apiUrl}/${userId}`);
  }

  getCurrentUserId(): number {
    return this.authService.currentUserValue?.id
      ? this.authService.currentUserValue.id
      : 0;
  }

  updateUser(user: User): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/update`, user);
  }

  deleteUser(userId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${userId}`);
  }

  followUser(userId: number, followerId: number): Observable<void> {
    return this.http.post<void>(
      `${this.apiUrl}/${userId}/follow/${followerId}`,
      {},
      { headers: this.headers }
    );
  }

  unfollowUser(userId: number, followeeId: number): Observable<void> {
    return this.http.post<void>(
      `${this.apiUrl}/${userId}/unfollow/${followeeId}`,
      {},
      { headers: this.headers }
    );
  }

  rateLocalGuide(
    guideId: number,
    travelerId: number,
    rating: number
  ): Observable<void> {
    const params = new HttpParams().set('rating', rating.toString());
    return this.http.post<void>(
      `${this.apiUrl}/${guideId}/rate/${travelerId}`,
      {},
      { headers: this.headers, params: params }
    );
  }

  getAllGuides(): Observable<Guide[]> {
    const httpHeaders = this.authService.getHttpHeaders();
    return this.http
      .get<ApiResponse>(`${this.apiUrl}/guides`, { headers: httpHeaders })
      .pipe(map((response) => response.data as Guide[] | []));
  }

  filterLocalGuideByCity(keyword: string): Observable<User[]> {
    return this.http.get<User[]>(
      `${this.apiUrl}/filter-guides-city?keyword=${keyword}`,
      { headers: this.headers }
    );
  }

  searchTravelers(keyword: string): Observable<User[]> {
    return this.http.get<User[]>(
      `${this.apiUrl}/search-traveler?keyword=${keyword}`,
      { headers: this.headers }
    );
  }

  getFollowers(userId: number): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/${userId}/followers`);
  }

  getProfile(userId: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${userId}/profile`, {
      headers: this.headers,
    });
  }

  getAllFollowing(userId: number | undefined): Observable<User[]> {
    return this.http
      .get<ApiResponse>(`${this.apiUrl}/${userId}/following`, {
        headers: this.headers,
      })
      .pipe(map((res) => (res.data ? (res.data as User[]) : [])));
  }

  getAverageRatingOfLocalGuide(userId: number): Observable<number> {
    return this.http
      .get<ApiResponse>(`${this.apiUrl}/${userId}/rating`, {
        headers: this.headers,
      })
      .pipe(map((res) => (res.data ? (res.data as number) : 0)));
  }

  getRatingCountOfLocalGuide(userId: number): Observable<number> {
    return this.http
      .get<ApiResponse>(`${this.apiUrl}/${userId}/rating-count`, {
        headers: this.headers,
      })
      .pipe(map((res) => (res.data ? (res.data as number) : 0)));
  }

  getTravellerId(): number {
    let traveller = this.authService.getUserFromLocalStorage();
    if (!traveller || !traveller.id) return -1;

    return traveller.id;
  }

  MOCK_TRIP_PREVIEWS: TripPreview[] = [
    {
      id: 1,
      name: 'Explore the Mountains',
      description:
        'Join us for an unforgettable adventure in the mountains. Experience breathtaking views, challenging hikes, and the serenity of nature.',
      link: '/trips/mountains', // Adjust the link as necessary for your routing
    },
    {
      id: 2,
      name: 'City Lights Tour',
      description:
        'Discover the vibrant life of the city after dark. This tour takes you through bustling streets, markets, and landmarks illuminated beautifully at night.',
      link: '/trips/city-lights',
    },
    {
      id: 3,
      name: 'Safari Adventure',
      description:
        'Get close to nature and wildlife on this exciting safari adventure. See wild animals in their natural habitat and learn about conservation efforts.',
      link: '/trips/safari',
    },
    {
      id: 4,
      name: 'Cultural Heritage Tour',
      description:
        'Explore the rich cultural heritage of the region. Visit historical sites, museums, and monuments that tell the story of the people and their traditions.',
      link: '/trips/cultural-heritage',
    },
  ];
  public getHttpHeadersWithRating(rating: number): HttpHeaders {
    const token = this.authService.getTokenFromLocalStorage();
    const httpOptions = new HttpHeaders({
      Authorization: `Bearer ${token}`,
      ' rating': rating.toString(),
    });
    return httpOptions;
  }
}
