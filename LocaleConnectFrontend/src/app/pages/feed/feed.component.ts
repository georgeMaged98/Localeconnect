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
  profileImageSrc = 'assets/pictures/not_available.png';
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

  //TODO: replace with api call
  likePost(post: Post): void {
    if (post.likedByUser) {
      // If already liked, unlike it and decrement the likes counter
      post.likes = (post.likes || 1) - 1;
      post.likedByUser = false;
    } else {
      // If not liked, like it and increment the likes counter
      post.likes = (post.likes || 0) + 1;
      post.likedByUser = true;
    }
    // Here, you would also call the service method to update the backend
  }

  //TODO: replace with api call
  toggleFollow(follower: any): void {
    follower.isFollowing = !follower.isFollowing;
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

  changeProfilePicture(event: any): void {
    if (event.target.files && event.target.files[0]) {
      const file = event.target.files[0];

      const reader = new FileReader();
      reader.onload = (e: any) => this.profileImageSrc = e.target.result;
      reader.readAsDataURL(file);
    }
  }

}



