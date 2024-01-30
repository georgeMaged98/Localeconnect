import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Review} from "../../model/review";
import {ReviewService} from "../../service/review.service";

@Component({
  selector: 'app-review',
  templateUrl: './review.component.html',
  styleUrls: ['./review.component.scss']
})
export class ReviewComponent implements OnInit {
 @Input() itineraryId: number=0;
  reviews: Review[] = [];
  @Input() rating = 0;
  @Output() ratingChange = new EventEmitter<number>();


  constructor(private reviewService: ReviewService) {}

  ngOnInit(): void {

  }
  onSelect(rating: number): void {
    this.rating = rating;
    this.ratingChange.emit(this.rating);
  }

  array(num: number): any[] {
    return Array(num);
  }
  loadReviews(): void {
    this.reviewService.getAllReviews(this.itineraryId).subscribe(
      (reviews: Review[]) => this.reviews = reviews
    );
  }

}
