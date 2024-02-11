import {
  ChangeDetectorRef,
  Component,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import { Itinerary, Tag } from '../../model/itinerary';
import { ItineraryService } from '../../service/itinerary.service';
import { ItineraryDialogComponent } from './itinerary-dialog/itinerary-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { debounceTime, distinctUntilChanged, Subscription } from 'rxjs';
import { UserService } from '../../service/user.service';
import { ReviewService } from '../../service/review.service';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { ImagesService } from '../../service/image.service';
import { NotificationService } from '../../service/notification.service';
import { GuideProfile } from '../../model/guide';
import { MatPaginator } from '@angular/material/paginator';
import { ApiResponse } from 'src/app/model/apiResponse';

@Component({
  selector: 'app-itinerary',
  templateUrl: './itinerary.component.html',
  styleUrls: ['./itinerary.component.scss'],
})
export class ItineraryComponent implements OnInit, OnDestroy {
  allItineraries: Itinerary[] = [];
  itineraries: Itinerary[] = [];
  filterItineraries: Itinerary[] = [];
  images: string[] = [];
  subscription: Subscription = new Subscription();
  searchControl = new FormControl('');
  filterForm: FormGroup;
  tagOptions: Tag[] = Object.values(Tag).filter((key) =>
    isNaN(Number(key))
  ) as Tag[];
  totalLength = 0;
  displayedItineraries: Itinerary[] = [];
  pageSize = 10;
  showAllImages = false;

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private notificationService: NotificationService,
    private imageService: ImagesService,
    private userService: UserService,
    private itineraryService: ItineraryService,
    private reviewService: ReviewService,
    public dialog: MatDialog,
    private cdr: ChangeDetectorRef,
    private formBuilder: FormBuilder
  ) {
    this.filterForm = this.formBuilder.group({
      place: [''],
      days: [''],
      tag: [''],
    });
  }

  ngOnInit(): void {
    this.initializeDisplayedItineraries();
    this.itineraryService.getItineraries().subscribe({
      next: (itineraryRes: ApiResponse) => {
        this.allItineraries = itineraryRes.responseObject as Itinerary[];
        this.itineraries = [...this.allItineraries];
        this.totalLength = this.allItineraries.length;
        this.initializeDisplayedItineraries();
        this.filterItineraries = [...this.allItineraries];
        this.allItineraries.forEach((itinerary) => {
          // itinerary.mappedTags = this.itineraryService.mapTags(itinerary.tags);
          // this.fetchUsername(itinerary);
          if (itinerary.imageUrls.length > 0) {
            if (itinerary.imageUrls[0].length > 0) {
              this.imageService.getImage(itinerary.imageUrls[0]).subscribe({
                next: (gcpRes: ApiResponse) => {
                  itinerary.imageUrls = [];
                  itinerary.imageUrls.push(gcpRes.responseObject.toString());
                },
                error: (errorMessage: ApiResponse) =>
                  console.error(errorMessage.errors),
              });
            }
          }
        });
      },
      error: (errorMessage: ApiResponse) => console.error(errorMessage.errors),
    });

    // this.imageService.currentImages.subscribe((images) => {
    //   this.images = images;
    // });

    // this.subscription = this.itineraryService.currentItinerary.subscribe(
    //   (itinerary) => {
    //     console.log('WEIRD', itinerary);

    //     if (itinerary) {
    //       this.addItinerary(itinerary);
    //       itinerary.imageUrls = this.images;
    //       this.fetchUsername(itinerary);
    //       this.updateDisplayedItineraries();
    //       this.cdr.detectChanges();
    //     }
    //   }
    // );

    this.searchControl.valueChanges
      .pipe(debounceTime(300), distinctUntilChanged())
      .subscribe((searchTerm) => {
        this.performSearch(searchTerm);
      });
  }
  checkUserItinerariesBeforeDeletion(itineraryId: number): void {
    this.itineraryService.getUserItineraries(this.userService.getCurrentUserId()).subscribe({
      next: (itineraries) => {
        const userHasItineraries = itineraries.some(itinerary => itinerary.id === itineraryId);
        if (userHasItineraries) {
          this.deleteItinerary(itineraryId);
        } else {
          this.notificationService.showSuccess('No permission to delete this itinerary or it doesn\'t belong to the user.');
        }
      },
      error: (error) => console.error('Error fetching user itineraries', error),
    });
  }

  deleteItinerary(id: number): void {
    const confirmDelete = confirm('Are you sure you want to delete this itinerary?');
    if (confirmDelete) {
      this.itineraryService.deleteItinerary(id).subscribe({
        next: () => {
          this.notificationService.showSuccess('Itinerary deleted successfully!');
          this.itineraries = this.itineraries.filter(itinerary => itinerary.id !== id);
          this.allItineraries = this.allItineraries.filter(itinerary => itinerary.id !== id);
          this.updateDisplayedItineraries();
        },
        error: (error) => {
          console.error('Error deleting itinerary', error);
          this.notificationService.showError('Failed to delete itinerary.');
        }
      });
    }
  }

  performSearch(searchTerm: string | null = ''): void {
    this.displayedItineraries = searchTerm
      ? this.itineraryService.searchItineraries(searchTerm, this.itineraries)
      : [...this.itineraries];
  }

  performFilter(): void {
    const filterValues = this.filterForm.value;
    const place = filterValues.place || null;
    const tag = filterValues.tag || null;
    const days = filterValues.days ? parseInt(filterValues.days, 10) : null;

    this.allItineraries = this.itineraryService.filterItineraries(
      this.itineraries,
      place,
      tag,
      days
    );
  }

  // addItinerary(newItinerary: Itinerary): void {
  //   console.log('ADD IT ', newItinerary);

  //   this.itineraryService.addItinerary(newItinerary).subscribe({
  //     next: (res: ApiResponse) => {
  //       console.log(res);

  //       // this.allItineraries.push(itinerary);
  //     },
  //     error: (e: any) => {
  //       console.error('Error adding itinerary', e);
  //     },
  //   });
  // }

  toggleDetails(itinerary: Itinerary): void {
    itinerary.expand = !itinerary.expand;
  }

  openAddItineraryDialog(): void {
    const dialogRef = this.dialog.open(ItineraryDialogComponent, {
      width: '600px',
      height: '600px',
    });

    dialogRef.afterClosed().subscribe((newItinerary: Itinerary) => {
      this.allItineraries.push(newItinerary);
    });
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  ngAfterViewInit() {
    this.paginator.page.subscribe(() => {
      this.updateDisplayedItineraries();
    });
  }

  initializeDisplayedItineraries(): void {
    this.displayedItineraries = this.itineraries.slice(0, this.pageSize);
  }

  updateDisplayedItineraries(): void {
    const startIndex = this.paginator.pageIndex * this.paginator.pageSize;
    const endIndex = startIndex + this.paginator.pageSize;
    this.displayedItineraries = this.allItineraries.slice(startIndex, endIndex);
  }


  fetchUsername(itinerary: Itinerary): void {
    // this.userService.getUsername(itinerary.userId).subscribe({
    //   next: (username: string) => {
    //     itinerary.username = username;
    //     this.cdr.markForCheck();
    //   },
    //   error: (error: any) => {
    //     console.error('Error fetching username', error);
    //   },
    // });
  }

  submitRating(itinerary: Itinerary, rating: number): void {
    if (itinerary.rating !== 0) {
      itinerary.ratingSubmitted = true;
      itinerary.rating = rating;
      if (
        itinerary.averageRating &&
        itinerary.totalRatings &&
        itinerary.totalRatings > 0
      ) {
        itinerary.averageRating =
          (itinerary.averageRating * itinerary.totalRatings + rating) /
          (itinerary.totalRatings + 1);
      } else {
        itinerary.averageRating = rating;
      }
      itinerary.totalRatings = (itinerary.totalRatings || 0) + 1;

      this.notificationService.showSuccess(
        'You submitted the review successfully!'
      );
      //TODO: uncomment for api call
      /*
      const review: Review = { userId: this.getTravellerId(), rating, entityId: itinerary.id, entityType: "itinerary" };
      this.reviewService.createReview(review).subscribe({
        next: () => {
          if (itinerary.averageRating && itinerary.totalRatings && itinerary.totalRatings > 0) {
            itinerary.averageRating = ((itinerary.averageRating * itinerary.totalRatings) + rating) / (itinerary.totalRatings + 1);
          } else {
            itinerary.averageRating = rating;
          }
          itinerary.totalRatings = (itinerary.totalRatings || 0) + 1;
        },
        error: (error) => console.error('Error submitting review', error)
      });

       */
    }
  }
  toggleImagesDisplay() {
    this.showAllImages = !this.showAllImages;
  }
}
