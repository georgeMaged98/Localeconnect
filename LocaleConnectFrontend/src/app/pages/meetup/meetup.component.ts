import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Meetup } from '../../model/meetup';
import { MeetupService } from '../../service/meetup.service';
import { FormControl } from '@angular/forms';
import { debounceTime, distinctUntilChanged, Subscription } from 'rxjs';
import { UserService } from '../../service/user.service';
import { ReviewService } from '../../service/review.service';
import { MatDialog } from '@angular/material/dialog';
import { MeetupDialogComponent } from './meetup-dialog/meetup-dialog.component';
import { NotificationService } from '../../service/notification.service';
import { MatPaginator } from '@angular/material/paginator';
import { GuideProfile } from '../../model/guide';
import { ApiResponse } from 'src/app/model/apiResponse';

@Component({
  selector: 'app-meetup',
  templateUrl: './meetup.component.html',
  styleUrls: ['./meetup.component.scss'],
})
export class MeetupComponent implements OnInit, OnDestroy {
  meetups: Meetup[] = [];
  searchMeetups: Meetup[] = [];
  searchControl = new FormControl('');
  subscription: Subscription = new Subscription();
  totalLength = 0;
  displayedMeetups: Meetup[] = [];
  pageSize = 10;

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private notificationService: NotificationService,
    private dialog: MatDialog,
    private reviewService: ReviewService,
    private meetupService: MeetupService,
    private userService: UserService
  ) {}

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  ngOnInit(): void {
    this.initializeDisplayedMeetups();
    this.meetupService.getAllMeetups().subscribe({
      next: (meetupRes: ApiResponse) => {
        this.meetups = meetupRes.data as Meetup[];
        this.meetups = [...this.meetups];
        this.totalLength = this.meetups.length;
        const travellerId = this.getTravellerId();
        this.meetups.forEach((meetup) => {
          if (meetup.meetupAttendees.includes(travellerId)) {
            meetup.isAttending = true;
          } else {
            meetup.isAttending = false;
          }
        });
        console.log(this.meetups);

        this.initializeDisplayedMeetups();
        this.searchMeetups = [...this.meetups];
      },
      error: (errorMessage: ApiResponse) => console.error(errorMessage.errors),
    });
    // this.meetups = this.meetupService.getAllMeetups();
    // this.totalLength = this.meetups.length;
    // this.initializeDisplayedMeetups();
    // this.searchMeetups = [...this.meetups];

    // //TODO: use this api call instead
    // this.meetupService.getAllMeetups().subscribe(data => {
    //   this.meetups = data;
    // });

    // this.subscription = this.meetupService.currentMeetup.subscribe(meetup => {
    //   if (meetup) {
    //     //TODO: replace mock with backend
    //     // this.addMeetup(meetup)
    //     this.addMeetupMock(meetup);
    //   }

    // });
    this.searchControl.valueChanges
      .pipe(debounceTime(300), distinctUntilChanged())
      .subscribe((searchTerm) => {
        this.performSearch(searchTerm);
      });
  }

  ngAfterViewInit() {
    this.paginator.page.subscribe(() => {
      this.updateDisplayedMeetups();
    });
  }
  initializeDisplayedMeetups(): void {
    this.displayedMeetups = this.meetups.slice(0, this.pageSize);
  }
  updateDisplayedMeetups(): void {
    const startIndex = this.paginator.pageIndex * this.paginator.pageSize;
    const endIndex = startIndex + this.paginator.pageSize;
    this.displayedMeetups = this.meetups.slice(startIndex, endIndex);
  }

  performSearch(searchTerm: string | null = ''): void {
    this.displayedMeetups = searchTerm
      ? this.meetupService.searchMeetups(searchTerm, this.searchMeetups)
      : [...this.searchMeetups];
  }

  attendMeetup(meetupId: number): void {
    const travellerId = this.getTravellerId();
    this.meetupService.attendMeetup(meetupId, travellerId).subscribe({
      next: (res: ApiResponse) => {
        if (res.status === 200) {
          const currentMeetup = this.meetups.find(
            (meetup) => meetup.id === meetupId
          );
          if (currentMeetup) currentMeetup.isAttending = true;
          this.initializeDisplayedMeetups();
        }
      },
      error: (errorMessage: ApiResponse) => console.error(errorMessage.errors),
    });
  }

  changeMeetupAttendance(meetupId: number): void {
    const currentMeetup = this.meetups.find((meetup) => meetup.id === meetupId);
    console.log(currentMeetup);
    if (currentMeetup?.isAttending) {
      this.unattendMeetup(meetupId);
    } else {
      this.attendMeetup(meetupId);
    }
  }

  // addMeetupMock(meetup: Meetup) {
  //   this.meetupService.createMeetup(meetup);
  //   this.meetups.push(meetup);
  //   this.totalLength = this.meetups.length;
  //   this.updateDisplayedMeetups();
  // }

  unattendMeetup(meetupId: number): void {
    const travellerId = this.getTravellerId();
    this.meetupService.unattendMeetup(meetupId, travellerId).subscribe({
      next: (res: ApiResponse) => {
        if (res.status === 200) {
          const currentMeetup = this.meetups.find(
            (meetup) => meetup.id === meetupId
          );
          if (currentMeetup) currentMeetup.isAttending = false;
          this.initializeDisplayedMeetups();
        }
      },
      error: (errorMessage: ApiResponse) => console.error(errorMessage.errors),
    });
  }

  toggleDetails(meetup: Meetup): void {
    meetup.expand = !meetup.expand;
  }

  submitRating(meetup: Meetup, rating: number): void {
    if (meetup.rating !== 0) {
      meetup.ratingSubmitted = true;
      meetup.rating = rating;
      if (
        meetup.averageRating &&
        meetup.totalRatings &&
        meetup.totalRatings > 0
      ) {
        meetup.averageRating =
          (meetup.averageRating * meetup.totalRatings + rating) /
          (meetup.totalRatings + 1);
      } else {
        meetup.averageRating = rating;
      }
      meetup.totalRatings = (meetup.totalRatings || 0) + 1;
      this.notificationService.showSuccess(
        'You submitted the review successfully!'
      );

      //TODO: uncomment for api call
      /*
      const review: Review = { userId: this.getTravellerId(), rating, entityId: meetup.id, entityType: "meetup" };
      this.reviewService.createReview(review).subscribe({
        next: () => {
          if (meetup.averageRating && meetup.totalRatings && meetup.totalRatings > 0) {
            meetup.averageRating = ((meetup.averageRating * meetup.totalRatings) + rating) / (meetup.totalRatings + 1);
          } else {
            meetup.averageRating = rating;
          }
          meetup.totalRatings = (meetup.totalRatings || 0) + 1;
        },
        error: (error) => console.error('Error submitting review', error)
      });

       */
    }
  }

  openAddMeetupDialog(): void {
    const dialogRef = this.dialog.open(MeetupDialogComponent, {
      width: '600px',
      height: '600px',
    });
    dialogRef.afterClosed().subscribe((newMeetup: Meetup) => {
      this.meetups.push(newMeetup);
      this.initializeDisplayedMeetups();
    });
  }

  private getTravellerId(): number {
    return 1;
  }
  toggleAttend(meetup: Meetup): void {
    meetup.isAttending = !meetup.isAttending;
    if (meetup.isAttending) {
      this.meetupService
        .attendMeetup(meetup.id, this.getTravellerId())
        .subscribe(() => {
          meetup.isAttending = false;
        });
    } else {
      this.meetupService
        .unattendMeetup(meetup.id, this.getTravellerId())
        .subscribe(() => {
          meetup.isAttending = true;
        });
    }
  }
}
