import {ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {Itinerary, Tag} from "../../model/itinerary";
import {ItineraryService} from "../../service/itinerary.service";
import {ItineraryDialogComponent} from "./itinerary-dialog/itinerary-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {debounceTime, distinctUntilChanged, of, Subscription, switchMap} from "rxjs";
import {UserService} from "../../service/user.service";
import {Review} from "../../model/review";
import {ReviewService} from "../../service/review.service";
import {FormControl} from "@angular/forms";

@Component({
  selector: 'app-itinerary',
  templateUrl: './itinerary.component.html',
  styleUrls: ['./itinerary.component.scss']
})
export class ItineraryComponent implements OnInit, OnDestroy {
  itineraries: Itinerary[] = [];
  subscription: Subscription = new Subscription();
  searchControl = new FormControl('');

  constructor(private userService: UserService, private itineraryService: ItineraryService, private reviewService: ReviewService, public dialog: MatDialog, private cdr: ChangeDetectorRef
  ) {
  }

  ngOnInit(): void {
    this.itineraries = this.itineraryService.getItinerariesMock();
    //TODO: replace mock with api
    /*this.itineraryService.getItineraries().subscribe({
        next: (data: Itinerary[]) => {
          this.itineraries = data;
        },
        error: (errorMessage: any) => console.error(errorMessage)
      }
    );

     */
    this.itineraries.forEach((itinerary) => {
      itinerary.mappedTags = this.itineraryService.mapTags(itinerary.tags);
      this.fetchUsername(itinerary);
    });


    this.subscription = this.itineraryService.currentItinerary.subscribe(itinerary => {
      if (itinerary) {
        //TODO: replace mock with backend
        // this.addItinerary(itinerary);
        this.addItineraryMock(itinerary);
        this.fetchUsername(itinerary);
        this.cdr.detectChanges();
      }

    });
    this.searchControl.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(searchTerm =>searchTerm? this.itineraryService.searchItineraries(searchTerm): of([]))
    ).subscribe(itineraries => {
      // Handle the itineraries returned from the search
      // Update your itineraries list
    });
  }
    performFilter(place?: string, tag?: Tag, days?: string): void {
      this.itineraryService.filterItineraries(place, tag, days).subscribe(itineraries => {
        // Handle the itineraries returned from the filter
        // Update your itineraries list
      });
  }

  addItineraryMock(itinerary: Itinerary) {
    this.itineraryService.addItinerary(itinerary);
    this.itineraries.push(itinerary);

  }

  addItinerary(newItinerary: Itinerary): void {
    this.itineraryService.addItinerary(newItinerary).subscribe({

        next: (itinerary: Itinerary) => {
          this.itineraries.push(itinerary);
        },
        error: (error: any) => {
          console.error('Error adding itinerary', error);
        }

      }
    );
  }


  toggleDetails(itinerary: Itinerary): void {
    itinerary.expand = !itinerary.expand;
  }

  openAddItineraryDialog(): void {
    const dialogRef = this.dialog.open(ItineraryDialogComponent, {
      width: '600px',
      height: '600px'
    });
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();

  }


  fetchUsername(itinerary: Itinerary): void {
    this.userService.getUsername(itinerary.userId).subscribe({
        next: (username: string) => {
          itinerary.username = username;
          this.cdr.markForCheck();
        },
        error: (error: any) => {
          console.error('Error fetching username', error);
        }
      }
    );
  }

  submitRating(itineraryId: number, rating: number): void {
    const review: Review = {
      itineraryId,
      //TODO: get user id
      userId: 0,
      rating,
    };
    this.reviewService.createReview(review).subscribe({
      next: (review) => {
        console.log('Review submitted successfully', review);
      },
      error: (error) => {
        console.error('Error submitting review', error);
      }
    });
  }

}
