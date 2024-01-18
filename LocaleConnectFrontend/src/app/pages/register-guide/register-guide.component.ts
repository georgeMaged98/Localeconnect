import { Component } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
  Validators
} from "@angular/forms";
import {AuthService} from "../../api/auth.service";
import {MatDialog} from "@angular/material/dialog";
import {LoginComponent} from "../login/login.component";

@Component({
  selector: 'app-register-guide',
  templateUrl: './register-guide.component.html',
  styleUrls: ['./register-guide.component.scss']
})
export class RegisterGuideComponent {
  registerForm: FormGroup;
  languagesList: string[] = [ 'Afrikaans', 'Albanian', 'Amharic', 'Arabic', 'Armenian', 'Azerbaijani', 'Basque', 'Belarusian', 'Bengali', 'Bosnian', 'Bulgarian',
    'Catalan', 'Cebuano', 'Chichewa', 'Chinese (Simplified)', 'Chinese (Traditional)', 'Corsican', 'Croatian', 'Czech', 'Danish', 'Dutch',
    'English', 'Esperanto', 'Estonian', 'Filipino', 'Finnish', 'French', 'Frisian', 'Galician', 'Georgian', 'German', 'Greek', 'Gujarati',
    'Haitian Creole', 'Hausa', 'Hawaiian', 'Hebrew', 'Hindi', 'Hmong', 'Hungarian', 'Icelandic', 'Igbo', 'Indonesian', 'Irish', 'Italian',
    'Japanese', 'Javanese', 'Kannada', 'Kazakh', 'Khmer', 'Korean', 'Kurdish (Kurmanji)', 'Kyrgyz', 'Lao', 'Latin', 'Latvian', 'Lithuanian',
    'Luxembourgish', 'Macedonian', 'Malagasy', 'Malay', 'Malayalam', 'Maltese', 'Maori', 'Marathi', 'Mongolian', 'Myanmar (Burmese)',
    'Nepali', 'Norwegian', 'Pashto', 'Persian', 'Polish', 'Portuguese', 'Punjabi', 'Romanian', 'Russian', 'Samoan', 'Scots Gaelic',
    'Serbian', 'Sesotho', 'Shona', 'Sindhi', 'Sinhala', 'Slovak', 'Slovenian', 'Somali', 'Spanish', 'Sundanese', 'Swahili', 'Swedish',
    'Tajik', 'Tamil', 'Telugu', 'Thai', 'Turkish', 'Ukrainian', 'Urdu', 'Uzbek', 'Vietnamese', 'Welsh', 'Xhosa', 'Yiddish', 'Yoruba', 'Zulu','Other'];
  constructor(private fb: FormBuilder, /*private authService: AuthService,*/public dialog: MatDialog) {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      dateOfBirth: ['', [Validators.required, adultValidator]],
      languages: ['', [Validators.required, minLanguagesValidator(2)]],
      termsAccepted: [false, Validators.requiredTrue]
    });
  }

  onSubmit(): void {
    if (this.registerForm.valid) {
      /*
      this.authService.register(this.registerForm.value).subscribe(
        success => console.log('Registration successful', success),
        error => console.error('Registration error', error)
      );

       */
    }
  }
  switchToLogin(): void {
    this.dialog.closeAll();
    this.dialog.open(LoginComponent, {
      width: '400px'
    });
  }

}

// Custom validator to check if the user is at least 18 years old
function adultValidator(control: FormControl): ValidationErrors | null {
  if (control.value) {
    const birthDate = new Date(control.value);
    const adultAge = 18;
    const currentDate = new Date();
    const eligibleDate = new Date(currentDate.getFullYear() - adultAge, currentDate.getMonth(), currentDate.getDate());

    if (birthDate > eligibleDate) {
      return { 'tooYoung': true };
    }
  }
  return null;
}
//custom validator to check if the user speaks more than one language
function minLanguagesValidator(min = 2): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const selected = control.value;
    return selected?.length >= min ? null : { 'minLanguages': { value: control.value } };
  };
}
