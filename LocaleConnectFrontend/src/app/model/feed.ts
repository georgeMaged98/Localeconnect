import {PostType} from "./post-type";
import {UserProfile} from "./user";

export interface Post {
  id?: number;
  author?: UserProfile | null;
  date?: string;
  authorID: number | undefined;
  content: string;
  images: string[];
  likedByUser?: boolean;
  comments?: Comment[];
  likes?: number;
  postType: PostType;
}
export interface Like {
  id?: number;
  likerId: number;
}
export interface Comment {
  id?: number;
  authorId: number;
  date: Date;
  text: string;
}
export interface FollowerProfile {
  userId?: number,
  name: string,
  username: string,
  isFollowing: boolean,
  profileImage: string;
}
