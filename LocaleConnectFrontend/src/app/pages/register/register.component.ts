import { Component } from '@angular/core';
import {LoginComponent} from "../login/login.component";
import {MatDialog} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../api/auth.service";
import {RegisterGuideComponent} from "../register-guide/register-guide.component";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  registerForm: FormGroup;

  constructor(private fb: FormBuilder,private authService: AuthService,public dialog: MatDialog) {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit(): void {
    if (this.registerForm.valid) {
      this.authService.registerTraveler(this.registerForm.value).subscribe(
        success => console.log('Registration successful', success),
        error => console.error('Registration error', error)
      );

    }
  }
  switchToLogin(): void {
    this.dialog.closeAll();
     this.dialog.open(LoginComponent, {
      width: '400px'
    });
  }

  openGuideDialog(): void {
    this.dialog.open(RegisterGuideComponent, {
      width: '400px'
    });
  }
}
