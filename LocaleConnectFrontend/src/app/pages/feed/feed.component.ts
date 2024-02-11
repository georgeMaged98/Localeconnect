import {Component, OnDestroy, OnInit} from '@angular/core';
import {FeedService} from "../../service/feed.service";
import {Comment, Post} from "../../model/feed";
import {AddPostDialogComponent} from "./add-post-dialog/add-post-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {map, Subject, Subscription, switchMap, takeUntil, tap} from "rxjs";
import {ImagesService} from "../../service/image.service";
import {User, UserProfile} from "../../model/user";
import {UserService} from "../../service/user.service";
import {AuthService} from "../../service/auth.service";

@Component({
  selector: 'app-feed',
  templateUrl: './feed.component.html',
  styleUrls: ['./feed.component.scss']
})
export class FeedComponent implements OnInit, OnDestroy {
  posts: Post[] = [];
  followers: any[] = [];
  profileImageSrc = 'assets/pictures/profil.png';
  newCommentTexts: { [key: number]: string } = {};
  showAllImages = false;
  subscription: Subscription = new Subscription();
  images: string[] = [];
  currentUserProfile: UserProfile | null = null;
  currentUser: User | null = null;
  private destroy$ = new Subject<void>();


  constructor(public dialog: MatDialog, private feedService: FeedService, private imageService: ImagesService, private userService: UserService, private authService: AuthService) {
  }

  ngOnInit(): void {
    this.authService.currentUser.subscribe(data => this.currentUser = data);
    if (this.authService.isAuthenticated()) {
      this.fetchCurrentUserAndFollowing();
    }
    this.fetchUserFeed();

    // this.imageService.currentImages.subscribe(images => {
    //   this.images = images;
    // });
  }

  fetchFollowing(): void {
    console.log(this.currentUserProfile);
    if (this.currentUserProfile?.id) {
      this.userService.getAllFollowingAsProfiles(this.currentUserProfile?.id)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: (profiles) => {
            console.log(profiles);
            this.followers = profiles;
          },
          error: (error) => console.error('Error fetching following profiles:', error)
        });
    }

  }

  fetchUserFeed(): void {
    const userId = this.authService.getUserIdFromLocalStorage();
    if (userId) {
      this.feedService.getUserFeed(userId).subscribe({
        next: (response) => {
          if (response) {
            this.posts = response;
          } else {
            console.error('Failed to fetch feed:', response);
          }
        },
        error: (error) => console.error('Error fetching feed:', error)
      });
    } else {
      console.error('User ID not found');
    }
  }

  fetchCurrentUserAndFollowing(): void {
    this.authService.fetchCurrentUserProfile().pipe(
      tap((currentUser: User) => {
        this.currentUserProfile = {
          id: currentUser.id,
          name: `${currentUser.firstName} ${currentUser.lastName}`,
          username: currentUser.userName,
          bio: currentUser.bio,
          imageUrl: currentUser.imageUrl,
        };
      }),
      switchMap((currentUser: User) =>
        this.userService.getAllFollowingAsProfiles(currentUser.id)
      ),
      map(users => users.map(user => ({
        userId: user.id,
        name: `${user.firstName} ${user.lastName}`,
        username: user.userName,
        isFollowing: false,
        profileImage: user.imageUrl || 'https://www.profilebakery.com/wp-content/uploads/2023/04/AI-Profile-Picture.jpg',
      }))),
      takeUntil(this.destroy$)
    ).subscribe({
      next: (profiles) => {
        this.followers = profiles;
      },
      error: (error) => console.error('Error fetching user and following profiles:', error)
    });
  }


  fetchCurrentUserProfile(): void {

    this.authService.fetchCurrentUserProfile().subscribe({
      next: (currentUser: User) => {
        this.currentUserProfile = {
          id: currentUser.id,
          name: `${currentUser.firstName} ${currentUser.lastName}`,
          username: currentUser.userName,
          bio: currentUser.bio,
          imageUrl: currentUser.imageUrl,
        };
        console.log(this.currentUserProfile);
      },
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


  //TODO: replace with api call
  toggleFollow(post: Post): void {
    //  post.author.isFollowing = !post.author.isFollowing;
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

  addComment(postId: number | undefined): void {
    if (postId && this.newCommentTexts[postId]) {
      const newComment: Comment = {
        // TODO: get comment from backend
        id: 0,
        authorId: 0,
        date: new Date(),
        text: this.newCommentTexts[postId],
      };

      const post = this.posts.find(p => p.id === postId);
      if (post) {
        // TODO: add comment to backend

       // post.comments.push(newComment);
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
    dialogRef.afterClosed().subscribe((newPost: Post) => {
      if (newPost) {
        this.posts.unshift(newPost);
      }
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

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}



