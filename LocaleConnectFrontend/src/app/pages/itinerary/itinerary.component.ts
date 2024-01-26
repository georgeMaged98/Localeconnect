import {ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {Itinerary} from "../../model/itinerary";
import {ItineraryService} from "../../api/itinerary.service";
import {ItineraryDialogComponent} from "./itinerary-dialog/itinerary-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-itinerary',
  templateUrl: './itinerary.component.html',
  styleUrls: ['./itinerary.component.scss']
})
export class ItineraryComponent implements OnInit, OnDestroy {
  itineraries: Itinerary[] = [];
  subscription: Subscription = new Subscription();

  constructor(private itineraryService: ItineraryService,public dialog: MatDialog, private cdr:ChangeDetectorRef
  ) {
  }

  ngOnInit(): void {
    this.itineraries = this.itineraryService.getItinerariesMock();
    this.subscription = this.itineraryService.currentItinerary.subscribe(itinerary => {
      if (itinerary) {
        this.addItineraryMock(itinerary);
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

}
