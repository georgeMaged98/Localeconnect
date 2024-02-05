import { Component } from '@angular/core';
import {RegisterComponent} from "../register/register.component";
import {MatDialog} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../api/auth.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  loginForm: FormGroup;

  constructor(private fb: FormBuilder, private authService: AuthService,public dialog: MatDialog) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      this.authService.login(this.loginForm.value).subscribe(
        {
          next: (user) => {
            return user;
          },
          error: (errorMessage) => {
            console.error("Login error", errorMessage);
            this.handleError(errorMessage);
          }
        }
      );

    }
  }
  openRegisterDialog(): void {
     this.dialog.open(RegisterComponent, {
      width: '400px', maxHeight:'600px'
    });
  }

  private handleError(errorMessage: string) {
    if (errorMessage.includes('username not found')) {
      this.loginForm.controls['username'].setErrors({userDoesntExist: true});
    }
    if (errorMessage.includes('wrong password')) {
      this.loginForm.controls['password'].setErrors({wrongPassword: true});
    }
  }
}
