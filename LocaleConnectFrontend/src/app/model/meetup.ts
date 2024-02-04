export interface Meetup {
  id: number;
  creatorName: string;
  name: string;
  description: string;
  date: Date;
  startTime: string;
  endTime: string;
  cost: number;
  location: string;
  spokenLanguages: string[];
  meetupAttendees: number[];
  rating: number;
  totalRatings?: number;
  averageRating?: number;
  expand?: boolean;
}
