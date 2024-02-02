//TODO: handle the followers
export interface Post {
  id: number;
  authorID: number;
  date: Date;
  content: string;
  images: string[];
  likes?: number;
  likedByUser?: boolean;
  isFollowingAuthor?: boolean;

  comments: Comment[];
}

export interface Comment {
  id: number;
  authorID: number;
  date: Date;
  text: string;
}
export interface Follower{
  id: number,
  name: string,
  handle: string,
  isFollowing: boolean
}
