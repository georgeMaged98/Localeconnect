import {Component, OnInit} from '@angular/core';
import {FormControl} from "@angular/forms";
import {debounceTime, distinctUntilChanged} from "rxjs";
import {MatDialog} from "@angular/material/dialog";
import {ReviewService} from "../../service/review.service";
import {UserService} from "../../service/user.service";
import {ImagesService} from "../../service/image.service";
import {GuideProfile} from "../../model/guide";

@Component({
  selector: 'app-guide',
  templateUrl: './guide.component.html',
  styleUrls: ['./guide.component.scss']
})
export class GuideComponent implements OnInit{
  guides: GuideProfile[] = [];
  searchGuides: GuideProfile[] = [];
  images: string[]=[];
  searchControl = new FormControl('');



  constructor(private imageService: ImagesService, private dialog: MatDialog, private reviewService: ReviewService, private userService: UserService) {
  }


  ngOnInit(): void {
    this.guides = this.userService.getGuidesMock();
    this.searchGuides = [...this.guides];

    //TODO: use this api call instead
    /*this.userService.getAllGuides().subscribe(data => {
    //TODO: map the data from backend
      this.guides = data;
    });

     */
    this.imageService.currentImages.subscribe(images => {
      this.images = images;
    });
    this.searchControl.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged(),
    ).subscribe(searchTerm => {
      this.performSearch(searchTerm);
    });
  }

  performSearch(searchTerm: string | null = ''): void {
    this.guides = searchTerm
      ? this.userService.searchGuides(searchTerm, this.searchGuides)
      : [...this.searchGuides];
  }


  toggleDetails(guide: GuideProfile): void {
    guide.expand = !guide.expand;
  }

  submitRating(guide: GuideProfile, rating: number): void {
    if (guide.rating !== 0) {
      guide.ratingSubmitted = true;
      guide.rating = rating;
      if (guide.averageRating && guide.totalRatings && guide.totalRatings > 0) {
        guide.averageRating = ((guide.averageRating * guide.totalRatings) + rating) / (guide.totalRatings + 1);
      } else {
        guide.averageRating = rating;
      }
      guide.totalRatings = (guide.totalRatings || 0) + 1;
      console.log(rating);
      //TODO: uncomment for api call
      /*
      const review: Review = { userId: guide.id, rating, entityId: guide.id, entityType: "guide" };
      this.reviewService.createReview(review).subscribe({
        next: () => {
          if (guide.averageRating && guide.totalRatings && guide.totalRatings > 0) {
            guide.averageRating = ((guide.averageRating * guide.totalRatings) + rating) / (guide.totalRatings + 1);
          } else {
            guide.averageRating = rating;
          }
          guide.totalRatings = (guide.totalRatings || 0) + 1;
        },
        error: (error) => console.error('Error submitting review', error)
      });

       */
    }
  }
  //TODO: uncomment for api call
  toggleFollow(guide : GuideProfile): void {
    guide.isFollowing = !guide.isFollowing;
    /*  if (guide.isFollowing) {
        this.userService.unfollowUser(guide.id).subscribe(() => {
          guide.isFollowing = false;
        });
      } else {
        this.userService.unfollowUser(guide.id).subscribe(() => {
          guide.isFollowing = true;
        });
      }
     */
  }
}
