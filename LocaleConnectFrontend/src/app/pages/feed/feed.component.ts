import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FeedService} from "../../service/feed.service";
import {Comment, Follower, Post} from "../../model/feed";

@Component({
  selector: 'app-feed',
  templateUrl: './feed.component.html',
  styleUrls: ['./feed.component.scss']
})
export class FeedComponent implements OnInit {
  posts: Post[] = [];
  followers: any[] = [];
  profileImageSrc = 'assets/pictures/profil.png';
  newCommentTexts: { [key: number]: string } = {};
  showAllImages = false;


  constructor(private feedService: FeedService) {
  }

  ngOnInit(): void {
    this.fetchPosts();
    this.fetchFollowers();
  }

  fetchPosts(): void {
    //TODO: replace with api call
    this.feedService.getPostsMock().subscribe({
        next: (data: Post[]) => {
          this.posts = data;
        },
        error: (err) => {
          console.error(err);
        }
      }
    );
  }

  fetchFollowers(): void {
    //TODO: replace with api call
    this.feedService.getFollowersMock().subscribe({
        next: (data: Follower[]) => {
          this.followers = data;
        },
        error: (err) => {
          console.error(err);
        }
      }
    );
  }

  //TODO: add api call
  likePost(post: Post): void {
    if (post.likedByUser) {
      post.likes = (post.likes || 1) - 1;
      post.likedByUser = false;
    } else {
      post.likes = (post.likes || 0) + 1;
      post.likedByUser = true;
    }
  }

  //TODO: replace with api call
  toggleFollow(post: Post): void {
    post.isFollowingAuthor = !post.isFollowingAuthor;
    /*  if (post.isFollowingAuthor) {
        this.feedService.unfollowUser(post.authorID).subscribe(() => {
          post.isFollowingAuthor = false;
        });
      } else {
        this.feedService.followUser(post.authorID).subscribe(() => {
          post.isFollowingAuthor = true;
        });
      }
     */
  }

  addComment(postId: number): void {
    if (this.newCommentTexts[postId]) {
      const newComment: Comment = {
        // TODO: get comment from backend
        id: 0,
        authorID: 1,
        date: new Date(),
        text: this.newCommentTexts[postId],
      };

      const post = this.posts.find(p => p.id === postId);
      if (post) {
        // TODO: add comment to backend

        post.comments.push(newComment);
        this.newCommentTexts[postId] = '';

      }
    }
  }

  toggleImagesDisplay() {
    this.showAllImages = !this.showAllImages;
  }
//TODO: configure image storage
  changeProfilePicture(event: any): void {
    if (event.target.files && event.target.files[0]) {
      const file = event.target.files[0];

      const reader = new FileReader();
      reader.onload = (e: any) => this.profileImageSrc = e.target.result;
      reader.readAsDataURL(file);
    }
  }

}



