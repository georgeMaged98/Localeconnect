
export interface TripPreview {
  id: string;
  name: string;
  description: string;
  link: string;
}
export interface Trip {
  localguideId?: number;
  name: string;
  description?: string;
  departureTime: string;
  destination: string;
  durationInDays: number;
  capacity: number;
  travelers: number[];
  languages: string[];
  placesToVisit: string[];
  dailyActivities?: string[];
  imageUrls?: string[];
  rating?: number;
  ratingSubmitted?: boolean;
  totalRatings?: number;
  averageRating?: number;
  expand?: boolean;
}

export interface TripReview {
  tripReviewId?: number;
  tripId: number;
  userId: number;
  text?: string;
  timestamp?: string;
  rating?: number;
}

export interface TripShare {
  id?: number;
  name: string;
  description?: string;
}
