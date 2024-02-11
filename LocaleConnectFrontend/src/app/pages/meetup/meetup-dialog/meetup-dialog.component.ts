import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { MeetupService } from '../../../service/meetup.service';
import { Meetup } from '../../../model/meetup';
import { LANGUAGES } from '../../../helper/DataHelper';
import { ApiResponse } from 'src/app/model/apiResponse';

@Component({
  selector: 'app-meetup-dialog',
  templateUrl: 'meetup-dialog.component.html',
  styleUrls: ['./meetup-dialog.component.scss'],
})
export class MeetupDialogComponent {
  meetupForm: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<MeetupDialogComponent>,
    private formBuilder: FormBuilder,
    private meetupService: MeetupService
  ) {
    this.meetupForm = this.formBuilder.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      date: ['', Validators.required],
      startTime: ['', [Validators.required, timeFormatValidator()]],
      endTime: ['', [Validators.required, timeFormatValidator()]],
      cost: ['', [Validators.min(0)]],
      location: ['', Validators.required],
      spokenLanguages: ['', Validators.required],
    });
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  onSubmit(): void {
    if (this.meetupForm.valid) {
      const formData = this.meetupForm.value;

      const newMeetup: Meetup = {
        ...formData,
        creatorId: 1,
        meetupAttendees: [],
      };

      this.meetupService.createMeetup(newMeetup).subscribe({
        next: (res: ApiResponse) => {
          console.log(res);
          this.dialogRef.close(res.responseObject);
        },
        error: (error: any) => console.error(error),
      });
    }
  }

  protected readonly LANGUAGES = LANGUAGES;
}

export function timeFormatValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value) {
      return null;
    }
    const isValidTime = /^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$/.test(control.value);
    return isValidTime ? null : { invalidTimeFormat: true };
  };
}
