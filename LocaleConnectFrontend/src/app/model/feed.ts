//TODO: handle the followers
export interface Post {
  id: number;
  authorID: number;
  date: Date;
  content: string;
  images: string[];
  comments: Comment[];
}

export interface Comment {
  id: number;
  authorID: number;
  date: Date;
  text: string;
  post?: Post;
}
