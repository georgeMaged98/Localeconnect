import {Component, OnInit} from '@angular/core';
import {Meetup} from "../../model/meetup";
import {MeetupService} from "../../service/meetup.service";
import {FormControl} from "@angular/forms";
import {debounceTime, distinctUntilChanged} from "rxjs";
import {UserService} from "../../service/user.service";
import {Review} from "../../model/review";
import {ReviewService} from "../../service/review.service";

@Component({
  selector: 'app-meetup',
  templateUrl: './meetup.component.html',
  styleUrls: ['./meetup.component.scss']
})
export class MeetupComponent implements OnInit {
  meetups: Meetup[] = [];
  searchMeetups: Meetup[] = [];
  private reviews: Review[] = [];
  isRatingSubmitted: boolean = false;

  searchControl = new FormControl('');


  constructor(private reviewService: ReviewService, private meetupService: MeetupService, private userService: UserService) {
  }

  ngOnInit(): void {
    this.meetups = this.meetupService.getMeetupsMocks();
    this.searchMeetups = [...this.meetups];

    //TODO: use this api call instead
    /*this.meetupService.getAllMeetups().subscribe(data => {
      this.meetups = data;
    });

     */
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

  unattendMeetup(meetupId: number): void {
    const travellerId = this.getTravellerId();
    this.meetupService.unattendMeetup(meetupId, travellerId).subscribe(() => {
    });
  }

  toggleDetails(meetup: Meetup): void {
    meetup.expand = !meetup.expand;
  }

  submitRating(meetup: Meetup, rating: number): void {
    if (!this.isRatingSubmitted) {
      this.isRatingSubmitted = true;

      const review: Review = {
        userId: 0,//TODO: get current user
        rating,
        entityId: meetup.id,
        entityType: "meetup"

      };
      this.reviewService.createReview(review).subscribe({
        next: (review) => {
          console.log('Review submitted successfully', review);
        },
        error: (error) => {
          console.error('Error submitting review', error);
        }
      });
      //TODO: replace with api call
      // const { averageRating, totalRatings } =this.reviewService.getRatingsForEntity(meetup.id,'meetup');
      if (meetup.totalRatings && meetup.totalRatings > 0) {
        meetup.averageRating = meetup.averageRating ? ((meetup.averageRating * meetup.totalRatings) + rating) / (meetup.totalRatings + 1) : rating;
        meetup.totalRatings += 1;
      }
      setTimeout(() => {
        this.isRatingSubmitted = false;
      }, 2000);
    }
  }

  private getTravellerId(): number {
    return 123;
  }
}
