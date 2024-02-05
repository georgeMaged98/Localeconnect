export interface User {
  id: number;
  firstName: string;
  lastName: string;
  userName: string;
  email: string;
  dateOfBirth: Date;
  bio?: string;
  password: string;
  visitedCountries: string[];
  registeredAsLocalGuide: boolean;
  languages: string[];
   followerIds:number[] ;
  followingIds: number[];

}
