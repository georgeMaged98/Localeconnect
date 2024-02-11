import {Component, OnInit, ViewChild} from '@angular/core';
import {FormControl} from '@angular/forms';
import {debounceTime, distinctUntilChanged} from 'rxjs';
import {MatDialog} from '@angular/material/dialog';
import {ReviewService} from '../../service/review.service';
import {UserService} from '../../service/user.service';
import {ImagesService} from '../../service/image.service';
import {GuideProfile} from '../../model/guide';
import {NotificationService} from '../../service/notification.service';
import {MatPaginator} from '@angular/material/paginator';
import {AuthService} from "../../service/auth.service";

@Component({
  selector: 'app-guide',
  templateUrl: './guide.component.html',
  styleUrls: ['./guide.component.scss'],
})
export class GuideComponent implements OnInit {
  guides: GuideProfile[] = [];
  searchGuides: GuideProfile[] = [];
  images: string[] = [];
  searchControl = new FormControl('');
  totalLength = 0;
  displayedGuides: GuideProfile[] = [];
  pageSize = 10;

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private notificationService: NotificationService,
    private imageService: ImagesService,
    private dialog: MatDialog,
    private reviewService: ReviewService,
    private userService: UserService,
    private authService: AuthService,
  ) {
  }

  ngOnInit(): void {
    this.fetchGuides();
    this.totalLength = this.guides.length;
    this.initializeDisplayedGuides();
    this.searchGuides = [...this.guides];
    this.searchControl.valueChanges
      .pipe(debounceTime(300), distinctUntilChanged())
      .subscribe((searchTerm) => {
        this.performSearch(searchTerm);
      });
  }

  fetchGuides(): void {
    this.userService.getAllGuides().subscribe({
      next: (guides) => {
        const userId = this.authService.getUserIdFromLocalStorage();
        if (userId) {
          this.userService.getAllFollowing(userId).subscribe(followingGuides => {
            this.guides = guides.map(guide => ({
              id: guide.id,
              name: `${guide.firstName} ${guide.lastName}`,
              userName: guide.userName,
              bio: guide.bio,
              visitedCountries: guide.visitedCountries,
              languages: guide.languages,
              city: guide.city,
              rating: guide.rating | 0,
              imageUrl: guide.imageUrl,
              isFollowing: followingGuides.some(followingGuide => followingGuide.id === guide.id)
            }));
            this.guides.forEach(guide => {
              this.fetchGuideRatings(guide);
              console.log(guide.averageRating)
            });
            this.initializeDisplayedGuides();
          });
        } else {
          this.guides = guides.map(guide => ({
            id: guide.id,
            name: `${guide.firstName} ${guide.lastName}`,
            userName: guide.userName,
            bio: guide.bio,
            visitedCountries: guide.visitedCountries,
            languages: guide.languages,
            city: guide.city,
            rating: guide.rating | 0,
            imageUrl: guide.imageUrl,
            isFollowing: false,
          }));
          this.guides.forEach(guide => {
            this.fetchGuideRatings(guide);
          });
          this.initializeDisplayedGuides();
        }
        }

      ,
      error: (error) => console.error('Error fetching guides:', error),
    });
    this.updateFollowingStatus();

  }

  fetchGuideRatings(guide: GuideProfile): void {
    if (guide.id
    ) {
      this.userService.getAverageRatingOfLocalGuide(guide.id).subscribe(averageRating => {
        guide.averageRating = averageRating;
        console.log(guide.averageRating)
      });
      this.userService.getRatingCountOfLocalGuide(guide.id).subscribe(ratingCount => {
        guide.totalRatings = ratingCount;
      });
    }

  }

  submitRating(guide:GuideProfile, rating: number): void {
    const userId = this.authService.getUserIdFromLocalStorage();
    if (userId && guide.id
    ) {

      this.userService.rateLocalGuide(guide.id, userId, rating).subscribe({
        next: () => this.notificationService.showSuccess('Rating submitted successfully'),
        error: () => this.notificationService.showError('Failed to submit rating'),
      });
      this.guides.forEach((guide) => {
        if (guide.id) {
          this.userService.getAverageRatingOfLocalGuide(guide.id).subscribe((averageRating) => {
            guide.averageRating = averageRating;
          });
          this.userService.getRatingCountOfLocalGuide(guide.id).subscribe((ratingCount) => {
            guide.totalRatings = ratingCount;
          });
        }

      });
      this.updateDisplayedGuides();
      guide.ratingSubmitted = true;
    }
  }

  updateFollowingStatus()
    :
    void {
    const currentUserId = this.authService.getUserIdFromLocalStorage();
    if (currentUserId) {
      this.userService.getAllFollowing(currentUserId).subscribe({
        next: (followingUsers) => {
          this.guides = this.guides.map(guide => ({
                ...guide,
                isFollowing: followingUsers.some(followingUser => followingUser.id === guide.id)
              }
            )
          );
        },
        error: (error) => console.error('Error fetching following status:', error)
      });
    }
  }

  ngAfterViewInit() {
    this.paginator.page.subscribe(() => {
      this.updateDisplayedGuides();
    });
  }

  initializeDisplayedGuides()
    :
    void {
    this.displayedGuides = this.guides.slice(0, this.pageSize);
  }

  updateDisplayedGuides()
    :
    void {
    const startIndex = this.paginator.pageIndex * this.paginator.pageSize;
    const endIndex = startIndex + this.paginator.pageSize;
    this.displayedGuides = this.guides.slice(startIndex, endIndex);
  }

  performSearch(searchTerm
                  :
                  string | null = ''
  ):
    void {
    this.displayedGuides = searchTerm
      ? this.userService.searchGuides(searchTerm, this.searchGuides)
      : [...this.searchGuides];
  }

  toggleDetails(guide
                  :
                  GuideProfile
  ):
    void {
    guide.expand = !guide.expand;
  }

  toggleFollow(guide
                 :
                 GuideProfile
  ):
    void {
    const action = guide.isFollowing ? this.userService.unfollowUser : this.userService.followUser;
    const currentUserId = this.authService.getUserIdFromLocalStorage();

    if (currentUserId && guide.id
    ) {
      action.call(this.userService, currentUserId, guide.id).subscribe({
        next: () => {
        guide.isFollowing ? this.notificationService.showSuccess('Unfollowed successfully!'):this.notificationService.showSuccess('Followed successfully!') ;
          guide.isFollowing = !guide.isFollowing;
        },
        error: (error) => {
          console.error('Error toggling follow status:', error);
          guide.isFollowing = !guide.isFollowing;
        }
      });
    }

  }
}
