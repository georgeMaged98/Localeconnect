
export interface Post {
  id: number;
  author: Profile;
  date: Date;
  content: string;
  images: string[];
  likes?: number;
  likedByUser?: boolean;

  comments: Comment[];
}

export interface Comment {
  id: number;
  author: Profile;
  date: Date;
  text: string;
}
export interface Profile {
  userId?: number,
  name: string,
  username: string,
  isFollowing: boolean,
  profileImage: string;
}
