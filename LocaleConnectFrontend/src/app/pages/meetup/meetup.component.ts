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
import { AuthService } from 'src/app/service/auth.service';
import { User } from 'src/app/model/user';
import { HttpErrorResponse } from '@angular/common/http';

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
    private userService: UserService,
    private authService: AuthService
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
        this.initializeDisplayedMeetups();
        this.searchMeetups = [...this.meetups];
      },
      error: (errorMessage: ApiResponse) => console.error(errorMessage.errors),
    });

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

  deleteMeetup(id: number): void {
    const confirmDelete = confirm(
      'Are you sure you want to delete this Meetup?'
    );

    if (confirmDelete) {
      this.meetupService.deleteMeetup(id).subscribe({
        next: () => {
          this.notificationService.showSuccess('Meetup deleted successfully!');
          this.meetups = this.meetups.filter(
            (itinerary) => itinerary.id !== id
          );
          this.updateDisplayedMeetups();
        },
        error: (error) => {
          console.error('Error deleting meetup', error);
          this.notificationService.showError('Failed to delete meetup.');
        },
      });
    }
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

  checkUserMeetupsBeforeDeletion(meetupId: number): void {
    this.authService.fetchCurrentUserProfile().subscribe({
      next: (user: User) => {
        const meetup = this.meetups.find((meetup) => meetup.id === meetupId);
        if (meetup?.creatorId === user.id) {
          this.deleteMeetup(meetupId);
        } else {
          this.notificationService.showError(
            'Meetup was created by another traveller!'
          );
        }
      },
      error: (error: HttpErrorResponse) => {
        console.error('Error fetching user profile', error);
        this.notificationService.showError(error.error.errors.errors[0]);
      },
    });
    // this.meetupService.getCreatorMeetups(this.userService.getCurrentUserId()).subscribe({
    //   next: (res) => {
    //     const meetups: Meetup[] = res.data as Meetup[];
    //     const userHasMeetups = meetups.some(itinerary => itinerary.id === id);
    //     if (userHasMeetups) {
    //       this.deleteMeetup(id);
    //     } else {
    //       this.notificationService.showSuccess('No permission to delete this itinerary or it doesn\'t belong to the user.');
    //     }
    //   },
    //   error: (error) => console.error('Error fetching user itineraries', error),
    // });
  }

  changeMeetupAttendance(meetupId: number): void {
    const currentMeetup = this.meetups.find((meetup) => meetup.id === meetupId);
    if (currentMeetup?.isAttending) {
      this.unattendMeetup(meetupId);
    } else {
      this.attendMeetup(meetupId);
    }
  }

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

      const userId = 1;
      this.meetupService.rateMeetup(meetup.id, userId, rating).subscribe({
        next: (res: ApiResponse) => {
          const updatedMeetup: Meetup = res.data as Meetup;
          meetup.averageRating = updatedMeetup.averageRating;
          meetup.ratingsCount = updatedMeetup.ratingsCount;
        },
        error: (error: HttpErrorResponse) => {
          this.notificationService.showError(error.error.errors.errors[0]);
        },
      });
      this.notificationService.showSuccess(
        'You submitted the review successfully!'
      );
      this.initializeDisplayedMeetups();
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
