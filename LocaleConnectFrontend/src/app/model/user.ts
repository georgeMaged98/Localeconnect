export interface User {
  id?: number;
  firstName: string;
  lastName: string;
  userName: string;
  password: string;
  email: string;
  dateOfBirth:Date;
  bio?: string;
  visitedCountries?: string[];
  languages?: string[];
  followerIds?: number[];
  followingIds?: number[];
  token?: string;
  imageUrl?: string;
  isEnabled?: boolean;
  role?: string;
}
