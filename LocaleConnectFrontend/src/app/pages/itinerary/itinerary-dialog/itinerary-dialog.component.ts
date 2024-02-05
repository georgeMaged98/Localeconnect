import {Component} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Itinerary, Tag} from "../../../model/itinerary";
import {ItineraryService} from "../../../service/itinerary.service";
import * as DataHelper from 'src/app/helper/DataHelper';
import {ImagesService} from "../../../service/image.service";
@Component({
  selector: 'app-itinerary-dialog',
  templateUrl: 'itinerary-dialog.component.html',
  styleUrls: ['./itinerary-dialog.component.scss']

})
export class ItineraryDialogComponent {
  itineraryForm: FormGroup;
  tagOptions :Tag[]= Object.values(Tag).filter(key => isNaN(Number(key))) as Tag[];


  constructor(private imageService: ImagesService,
    public dialogRef: MatDialogRef<ItineraryDialogComponent>,
    private formBuilder: FormBuilder, private itineraryService: ItineraryService
  ) {
    this.itineraryForm = this.formBuilder.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      numberOfDays: ['1', [Validators.required, Validators.min(1)]],
      tags: [''],
      placesToVisit: ['', Validators.required],
      dailyActivities: [''],
      image: ['']
    });
  }

  onNoClick(): void {
    this.dialogRef.close(null);
  }

  onSubmit(): void {
    if (this.itineraryForm.valid) {
      const formData = this.itineraryForm.value;
      formData.placesToVisit = DataHelper.dataToList(formData.placesToVisit);
      formData.dailyActivities = DataHelper.dataToList(formData.dailyActivities);



      const itinerary: Itinerary = {
        id: 0, //TODO: get from backend
        userId: 0,  //TODO: get from the user
        name: formData.name,
        username: '',
        description: formData.description,
        numberOfDays: formData.numberOfDays,
        tags:formData.tags,
        mappedTags:formData.tags,
        placesToVisit: formData.placesToVisit,
        dailyActivities: formData.dailyActivities,
        expand: false,
        imageUrls: formData.image,
        rating: 0
      };

      this.itineraryService.changeItinerary(itinerary);
      this.dialogRef.close();
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
            this.imageService.updateImages(images);
          }
        };
        reader.readAsDataURL(<Blob>file);
      });
    }
  }


}
