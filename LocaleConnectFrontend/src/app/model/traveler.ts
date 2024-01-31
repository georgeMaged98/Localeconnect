import {User} from "./user";

export interface Traveler extends User {
  interests: string[];
  travelerStyle: string;
}
