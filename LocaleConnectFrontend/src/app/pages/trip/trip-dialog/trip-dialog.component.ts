import {Component} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {TripService} from '../../../service/trip.service';
import {LANGUAGES} from '../../../helper/DataHelper';
import {Trip} from "../../../model/trip";
import * as DataHelper from 'src/app/helper/DataHelper';

@Component({
  selector: 'app-trip-dialog',
  templateUrl: './trip-dialog.component.html',
  styleUrls: ['./trip-dialog.component.scss']
})
export class TripDialogComponent {
  tripForm: FormGroup;
  readonly LANGUAGES = LANGUAGES;

  constructor(
    public dialogRef: MatDialogRef<TripDialogComponent>,
    private formBuilder: FormBuilder,
    private tripService: TripService,
  ) {
    this.tripForm = this.formBuilder.group({
      name: ['', Validators.required],
      description: [''],
      departureTime: ['', Validators.required],
      destination: ['', Validators.required],
      durationInDays: ['', [Validators.required, Validators.min(1)]],
      capacity: ['', [Validators.required, Validators.min(1)]],
      languages: ['', Validators.required],
      placesToVisit: ['', Validators.required],
      dailyActivities: [''],
      imageUrls: []
    });
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  onSubmit(): void {
    if (this.tripForm.valid) {
      const formData = this.tripForm.value;
      formData.placesToVisit = DataHelper.dataToList(formData.placesToVisit);
      formData.dailyActivities = DataHelper.dataToList(formData.dailyActivities);
      const newTrip: Trip = {
        ...formData,
        localguideId: 0,
      };
      console.log(newTrip);
      this.tripService.createTrip(newTrip).subscribe({
        next: (trip) => {
          this.tripService.changeTrip(newTrip);
          console.log(trip);
          this.dialogRef.close(trip);
        },
        error: (error) => {
          console.error('Error creating trip:', error);
        }
      });
    }
  }

  onFileSelected(event: any) {
    const files = event.target.files;
    const images: string[] = [];

    if (files) {
      Array.from(files).forEach((file) => {
        const reader = new FileReader();
        reader.onload = (e: any) => {
          images.push(e.target.result);
          if (images.length === files.length) {
            this.tripForm.value.imageUrls = images;
          }
        };
        reader.readAsDataURL(<Blob>file);
      });
    }
  }
}
