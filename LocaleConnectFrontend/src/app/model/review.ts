export interface Review {
  id?: number;
  itineraryId: number;
  userId: number;
  text?: string;
  timestamp?: string;
  rating: number;
}
