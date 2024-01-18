import { Component } from '@angular/core';
import {LoginComponent} from "../login/login.component";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  constructor(public dialog: MatDialog) {}

  switchToLogin(): void {
    this.dialog.closeAll();
    const loginDialogRef = this.dialog.open(LoginComponent, {
      width: '400px'
    });
  }
}
