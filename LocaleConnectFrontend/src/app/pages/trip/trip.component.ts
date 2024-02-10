import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {debounceTime, distinctUntilChanged, Subscription} from "rxjs";
import {FormControl} from "@angular/forms";
import {MatPaginator} from "@angular/material/paginator";
import {NotificationService} from "../../service/notification.service";
import {UserService} from "../../service/user.service";
import {ReviewService} from "../../service/review.service";
import {MatDialog} from "@angular/material/dialog";
import {Trip} from "../../model/trip";
import {TripService} from "../../service/trip.service";
import {TripDialogComponent} from "./trip-dialog/trip-dialog.component";
import {Meetup} from "../../model/meetup";

@Component({
  selector: 'app-trip',
  templateUrl: './trip.component.html',
  styleUrls: ['./trip.component.scss']
})
export class TripComponent implements OnInit, OnDestroy {
  trips: Trip[] = [];
  searchTrips: Trip[] = [];
  searchControl = new FormControl('');
  subscription: Subscription = new Subscription();
  totalLength = 0;
  displayedTrips: Trip[] = [];
  pageSize = 10;
  showAllImages = false;


  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private notificationService: NotificationService, private dialog: MatDialog, private reviewService: ReviewService, private tripService: TripService, private userService: UserService) {
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  ngOnInit(): void {
    this.tripService.getAllTrips().subscribe(data => {
      this.trips = data;
      this.totalLength = this.trips.length;
      this.initializeDisplayedTrips();
      this.searchTrips = [...this.trips];
    });


    this.subscription = this.tripService.currentTrip.subscribe(trip => {
      if (trip) {
        this.trips.push(trip);
        this.totalLength = this.trips.length;
        this.updateDisplayedTrips();
      }

    });
    this.searchControl.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged(),
    ).subscribe(searchTerm => {
      this.performSearch(searchTerm);
    });
  }

  ngAfterViewInit() {
    this.paginator.page.subscribe(() => {
      this.updateDisplayedTrips();
    });
  }

  initializeDisplayedTrips(): void {
    this.displayedTrips = this.trips.slice(0, this.pageSize);
  }

  updateDisplayedTrips(): void {
    const startIndex = this.paginator.pageIndex * this.paginator.pageSize;
    const endIndex = startIndex + this.paginator.pageSize;
    this.displayedTrips = this.trips.slice(startIndex, endIndex);
  }

  checkGuideTripsAndDelete(id: number): void {
    this.tripService.getLocalguideTrips(this.userService.getCurrentUserId()).subscribe({
      next: (trips) => {
        const userHasTrips = trips.some(trip => trip.id === id);
        if (userHasTrips) {
          this.deleteTrip(id);
        } else {
          this.notificationService.showSuccess('No permission to delete this Trip or it doesn\'t belong to the user.');
        }
      },
      error: (error) => console.error('Error fetching user trips', error),
    });
  }
  deleteTrip(id: number): void {
    const confirmDelete = confirm('Are you sure you want to delete this trip?');
    if (confirmDelete) {
      this.tripService.deleteTrip(id).subscribe({
        next: () => {
          this.notificationService.showSuccess('Trip deleted successfully!');
          this.trips = this.trips.filter(trip => trip.id !== id);
          this.updateDisplayedTrips();
        },
        error: (error) => {
          console.error('Error deleting itinerary', error);
          this.notificationService.showError('Failed to delete itinerary.');
        }
      });
    }
  }
  performSearch(searchTerm: string | null = ''): void {
    if (searchTerm) {
      this.tripService.searchTrip(searchTerm).subscribe(data => {
        this.displayedTrips = data;
      })
    } else {
      this.displayedTrips = [...this.searchTrips];

    }
  }

  toggleDetails(trip: Trip): void {
    trip.expand = !trip.expand;
  }

  submitRating(trip: Trip, rating: number): void {
    if (trip.rating !== 0) {
      trip.ratingSubmitted = true;
      trip.rating = rating;
      if (trip.averageRating && trip.totalRatings && trip.totalRatings > 0) {
        trip.averageRating = ((trip.averageRating * trip.totalRatings) + rating) / (trip.totalRatings + 1);
      } else {
        trip.averageRating = rating;
      }
      trip.totalRatings = (trip.totalRatings || 0) + 1;
      this.notificationService.showSuccess('You submitted the review successfully!')

      //TODO: uncomment for api call
      /*
      const review: Review = { userId: this.getTravellerId(), rating, entityId: trip.id, entityType: "trip" };
      this.reviewService.createReview(review).subscribe({
        next: () => {
          if (trip.averageRating && trip.totalRatings && trip.totalRatings > 0) {
            trip.averageRating = ((trip.averageRating * trip.totalRatings) + rating) / (trip.totalRatings + 1);
          } else {
            trip.averageRating = rating;
          }
          trip.totalRatings = (trip.totalRatings || 0) + 1;
        },
        error: (error) => console.error('Error submitting review', error)
      });

       */
    }
  }
  joinTrip(tripId: number): void {
    //TODO: add api call to get current traveler id
    const travellerId = 0;
    this.tripService.joinTrip(tripId, travellerId).subscribe(() => {
    });
  }

  leaveTrip(tripId: number): void {
    //TODO: add api call to get current traveler id
    const travellerId = 0;
    this.tripService.leaveTrip(tripId, travellerId).subscribe(() => {
    });
  }
  openAddMeetupDialog(): void {
    const dialogRef = this.dialog.open(TripDialogComponent, {
      width: '600px',
      height: '600px'
    });
  }
  toggleJoin(trip : Trip): void {
    //TODO: get id from current user
    trip.isAttending = !trip.isAttending;
     if (trip.isAttending) {
        this.tripService.joinTrip(trip.id, 0).subscribe(() => {
          trip.isAttending = false;
        });
      } else {
        this.tripService.leaveTrip(trip.id,0).subscribe(() => {
          trip.isAttending = true;
        });
      }
  }
  toggleImagesDisplay() {
    this.showAllImages = !this.showAllImages;
  }
}
