import {Component, OnDestroy, OnInit} from '@angular/core';
import {Meetup} from "../../model/meetup";
import {MeetupService} from "../../service/meetup.service";
import {FormControl} from "@angular/forms";
import {debounceTime, distinctUntilChanged, Subscription} from "rxjs";
import {UserService} from "../../service/user.service";
import {Review} from "../../model/review";
import {ReviewService} from "../../service/review.service";
import {ItineraryDialogComponent} from "../itinerary/itinerary-dialog/itinerary-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {MeetupDialogComponent} from "./meetup-dialog/meetup-dialog.component";
import {Itinerary} from "../../model/itinerary";

@Component({
  selector: 'app-meetup',
  templateUrl: './meetup.component.html',
  styleUrls: ['./meetup.component.scss']
})
export class MeetupComponent implements OnInit, OnDestroy {
  meetups: Meetup[] = [];
  searchMeetups: Meetup[] = [];
  searchControl = new FormControl('');
  subscription: Subscription = new Subscription();


  constructor(private dialog: MatDialog, private reviewService: ReviewService, private meetupService: MeetupService, private userService: UserService) {
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
    }

  ngOnInit(): void {
    this.meetups = this.meetupService.getMeetupsMocks();
    this.searchMeetups = [...this.meetups];

    //TODO: use this api call instead
    /*this.meetupService.getAllMeetups().subscribe(data => {
      this.meetups = data;
    });

     */
    this.subscription = this.meetupService.currentMeetup.subscribe(meetup => {
      if (meetup) {
        //TODO: replace mock with backend
        // this.addMeetup(meetup)
        this.addMeetupMock(meetup);
      }

    });
    this.searchControl.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged(),
    ).subscribe(searchTerm => {
      this.performSearch(searchTerm);
    });
  }

  performSearch(searchTerm: string | null = ''): void {
    this.meetups = searchTerm
      ? this.meetupService.searchMeetups(searchTerm, this.searchMeetups)
      : [...this.searchMeetups];
  }

  attendMeetup(meetupId: number): void {
    //TODO: add api call to get current traveler id
    const travellerId = this.getTravellerId();
    this.meetupService.attendMeetup(meetupId, travellerId).subscribe(() => {
    });
  }

  addMeetupMock(meetup: Meetup) {
    this.meetupService.createMeetup(meetup);
    this.meetups.push(meetup);

  }

  unattendMeetup(meetupId: number): void {
    const travellerId = this.getTravellerId();
    this.meetupService.unattendMeetup(meetupId, travellerId).subscribe(() => {
    });
  }

  toggleDetails(meetup: Meetup): void {
    meetup.expand = !meetup.expand;
  }

  submitRating(meetup: Meetup, rating: number): void {
    if (meetup.rating !== 0) {
      meetup.ratingSubmitted = true;
      meetup.rating = rating;
      if (meetup.averageRating && meetup.totalRatings && meetup.totalRatings > 0) {
        meetup.averageRating = ((meetup.averageRating * meetup.totalRatings) + rating) / (meetup.totalRatings + 1);
      } else {
        meetup.averageRating = rating;
      }
      meetup.totalRatings = (meetup.totalRatings || 0) + 1;
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
      height: '600px'
    });
  }

  private getTravellerId(): number {
    return 123;
  }
}
