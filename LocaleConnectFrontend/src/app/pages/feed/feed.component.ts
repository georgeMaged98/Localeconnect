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
import {getFormattedDateAndTime} from "../../helper/DateHelper";
import {coerceStringArray} from "@angular/cdk/coercion";

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
  disableLike: boolean = false;


  constructor(public dialog: MatDialog, private feedService: FeedService, private imageService: ImagesService, private userService: UserService, private authService: AuthService) {
  }

  ngOnInit(): void {
    this.authService.currentUser.subscribe(data => this.currentUser = data);
    if (this.authService.isAuthenticated()) {
      this.fetchCurrentUserAndFollowing();
      this.checkUserLikes();
      this.fetchUserFeed();

    }


  }


  fetchUserFeed(): void {
    const userId = this.authService.getUserIdFromLocalStorage();
    console.log(userId)
    if (userId) {
      this.feedService.getUserFeed(userId).subscribe({
        next: (response) => {
          if (response) {
            this.posts = response;

            this.posts.forEach(post => {
              this.fetchAuthorNamesForPosts(post);

              if (post.id) {
                this.refreshPostLikes(post.id)
                this.feedService.getPostLikeCount(post.id).subscribe({
                  next: (count) => {
                    console.log(count);
                    post.likes = count;
                  },
                  error: (error) => console.error('Error fetching like count:', error)
                });
                post.comments?.forEach(comment => {
                  this.fetchAuthorNamesForComments(comment);
                })
              }
            });
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
        this.userService.getAllFollowing(currentUser.id)
      ),
      map(users => users.map(user => ({
        userId: user.id,
        name: `${user.firstName} ${user.lastName}`,
        username: user.userName,
        isFollowing: false,
        profileImage: user.imageUrl || 'assets/pictures/profil.png',
      }))),
      takeUntil(this.destroy$)
    ).subscribe({
      next: (profiles) => {
        this.followers = profiles;
      },
      error: (error) => console.error('Error fetching user and following profiles:', error)
    });
  }


  fetchAuthorNamesForPosts(post: Post): void {
    if (post.authorID) {
      this.userService.getProfile(post.authorID).subscribe(user => {
        post.author = {
          name: `${user.firstName} ${user.lastName}`,
          username: user.userName
        }
      });
    }
  }

  fetchAuthorNamesForComments(comment: Comment): void {
    if (comment.authorID) {
      this.userService.getProfile(comment.authorID).subscribe(user => {
        comment.authorName = `${user.firstName} ${user.lastName}`;
      });
    }
  }

  likePost(post: Post): void {
    const likerId = this.authService.getUserIdFromLocalStorage();
    if (likerId && post.id && !post.likedByUser) {
      this.feedService.likePost(post.id, likerId).subscribe({
        next: () => {
        if(post.id){
          post.likedByUser = true;
          this.refreshPostLikes(post.id);
        }
        },
        error: (error) => console.error('Error liking post:', error)
      });
    }
  }
  refreshPostLikes(postId: number): void {
    this.feedService.getPostLikeCount(postId).subscribe(likeCount => {
      const post = this.posts.find(p => p.id === postId);
      if (post) {
        post.likes = likeCount;
      }
    });
    this.checkUserLikes();
  }

  checkUserLikes(): void {
    const username = this.authService.getUsernameFromLocalStorage();
    console.log(username)
    if (username) {
      this.posts.forEach(post => {
        if (post.id) {
          console.log(post)
          this.feedService.getPostLikes(post.id).subscribe(usersLiked => {
            console.log(usersLiked);
            post.likedByUser = usersLiked.includes(username);
          });
        }
      });
    }

  }

  addComment(postId: number): void {
    if (postId && this.newCommentTexts[postId]) {
      const currentUser = this.authService.getCurrentUserProfile(); // Assuming this method exists and returns current user's info
      const newComment: Comment = {
        authorID: this.authService.getUserIdFromLocalStorage() || 0,
        text: this.newCommentTexts[postId],
        date: getFormattedDateAndTime(new Date()),
        authorName: currentUser ? currentUser.name : 'Unknown User',
      };

      // Call the service to add the comment
      this.feedService.addComment(postId, newComment).subscribe({
        next: () => {
          const post = this.posts.find(p => p.id === postId);
          if (post) {
            if (!post.comments) post.comments = [];
            post.comments.push({ ...newComment, authorName: newComment.authorName });
            this.newCommentTexts[postId] = '';
          }
        },
        error: (error) => console.error('Error adding comment:', error),
      });
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
        newPost.author = this.authService.getCurrentUserProfile()
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



