import { Component } from '@angular/core';
import {RegisterComponent} from "../register/register.component";
import {MatDialog} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

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
     /* this.authService.login(this.loginForm.value).subscribe(
        success => console.log('Login successful', success),
        error => console.error('Login error', error)
      );

      */
    }
  }
  openRegisterDialog(): void {
     this.dialog.open(RegisterComponent, {
      width: '400px'
    });
  }
}
