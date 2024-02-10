import {Component, OnInit} from '@angular/core';
import {FeedService} from "../../service/feed.service";
import {Comment, Post, Profile} from "../../model/feed";
import {AddPostDialogComponent} from "./add-post-dialog/add-post-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {Subscription} from "rxjs";
import {ImagesService} from "../../service/image.service";
import {User, UserProfile} from "../../model/user";
import {UserService} from "../../service/user.service";
import {AuthService} from "../../service/auth.service";

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
  subscription: Subscription = new Subscription();
  images: string[] = [];
  currentUserProfile: UserProfile | null = null;


  constructor(public dialog: MatDialog, private feedService: FeedService, private imageService: ImagesService, private userService: UserService, private authService: AuthService) {
  }

  ngOnInit(): void {
    if (this.authService.isAuthenticated()) {
     this.fetchCurrentUserProfile();
    }
    this.fetchPosts();
    this.fetchFollowers();
    this.subscription = this.feedService.currentPost.subscribe(post => {
      if (post) {
        //TODO: replace mock with backend
        // this.addPost()
        this.addPostMock(post);
      }

    });
    // this.imageService.currentImages.subscribe(images => {
    //   this.images = images;
    // });
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
        next: (data: Profile[]) => {
          this.followers = data;
        },
        error: (err) => {
          console.error(err);
        }
      }
    );
  }

  fetchCurrentUserProfile(): void {

    this.authService.fetchCurrentUserProfile().subscribe({
      next: (currentUser: User) => {
        this.currentUserProfile = {
          name: `${currentUser.firstName} ${currentUser.lastName}`,
          username: currentUser.userName,
          bio: currentUser.bio,
          imageUrl: currentUser.imageUrl,
        };      },
      error: (error) => {
        console.error('Error fetching user profile:', error);
      }
    });
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

  addPostMock(post: Post) {
    this.feedService.createPost(post);
    this.posts.push(post);

  }

  //TODO: replace with api call
  toggleFollow(post: Post): void {
    post.author.isFollowing = !post.author.isFollowing;
    /*  if (post.author.isFollowing) {
        this.feedService.unfollowUser(post.author.userId).subscribe(() => {
          post.author.isFollowing = false;
        });
      } else {
        this.feedService.followUser(post.author.userId).subscribe(() => {
          post.author.isFollowing = true;
        });
      }
     */
  }

  addComment(postId: number): void {
    if (this.newCommentTexts[postId]) {
      const newComment: Comment = {
        // TODO: get comment from backend
        id: 0,
        author: {
          userId: 1,
          name: 'Alice Johnson',
          username: 'alicej',
          isFollowing: true,
          profileImage: 'path/to/alice.jpg',
        },
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

  openAddPostDialog(): void {
    const dialogRef = this.dialog.open(AddPostDialogComponent, {
      width: '500px',
    });
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



