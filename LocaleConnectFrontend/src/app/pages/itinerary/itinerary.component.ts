import {ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {Itinerary, Tag} from "../../model/itinerary";
import {ItineraryService} from "../../service/itinerary.service";
import {ItineraryDialogComponent} from "./itinerary-dialog/itinerary-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {Subscription} from "rxjs";
import {UserService} from "../../service/user.service";
import {Review} from "../../model/review";
import {ReviewService} from "../../service/review.service";

@Component({
  selector: 'app-itinerary',
  templateUrl: './itinerary.component.html',
  styleUrls: ['./itinerary.component.scss']
})
export class ItineraryComponent implements OnInit, OnDestroy {
  itineraries: Itinerary[] = [];
  subscription: Subscription = new Subscription();
  constructor(private userService: UserService, private itineraryService: ItineraryService,private reviewService: ReviewService, public dialog: MatDialog, private cdr: ChangeDetectorRef
  ) {
  }

  ngOnInit(): void {
    this.itineraries = this.itineraryService.getItinerariesMock();
    this.itineraries.forEach((itinerary) => {
      this.fetchUsername(itinerary);
    });

    this.subscription = this.itineraryService.currentItinerary.subscribe(itinerary => {
      if (itinerary) {
        this.addItineraryMock(itinerary);
        this.fetchUsername(itinerary);
        this.cdr.detectChanges();
      }

    });


    /*  this.itineraryService.getItineraries().subscribe({
        next: (data: Itinerary[]) => {
          this.itineraries = data;
        },
     //   error: (errorMessage: any) => console.error(errorMessage)
      }

      );

     */

  }

  addItineraryMock(itinerary: Itinerary) {
    this.itineraryService.addItinerary(itinerary);
    this.itineraries.push(itinerary);

  }

  /* addItinerary(newItinerary: Itinerary): void {
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

   */

  toggleDetails(itinerary: Itinerary): void {
    itinerary.expand = !itinerary.expand;
  }

  openAddItineraryDialog(): void {
    const dialogRef = this.dialog.open(ItineraryDialogComponent, {
      width: '400px',
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
    // Construct the ReviewDTO
    const review: Review = {
      itineraryId,
      //TODO: get user id
      userId: 0, // Replace with the actual user ID
      rating,
    };
    this.reviewService.createReview(review).subscribe({
      next: (review) => {
        console.log('Review submitted successfully', review);
        // Update local state if necessary
      },
      error: (error) => {
        console.error('Error submitting review', error);
      }
    });
  }

}
