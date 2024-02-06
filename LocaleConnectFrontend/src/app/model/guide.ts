import {User} from "./user";

export interface Guide extends User{
  city: string;
  rating: number;
}
//TODO: map from guide profile to guide and vices versa
export interface GuideProfile{
  id: number;
  name: string;
  userName: string;
  bio?: string;
  visitedCountries: string[];
  languages: string[];
  city: string;
  rating: number;
  ratingSubmitted?: boolean;
  totalRatings?: number;
  averageRating?: number;
  expand?: boolean;
  //TODO: replace with trips organized
  trips?: string;
  isFollowing?: boolean;
  imageUrl?: string;
}
