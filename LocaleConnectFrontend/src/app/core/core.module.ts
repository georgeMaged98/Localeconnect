import {NgModule} from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import {RouterModule} from '@angular/router';
import {HeaderComponent} from "./header/header.component";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {FooterComponent} from "./footer/footer.component";

@NgModule({
  declarations: [HeaderComponent, FooterComponent],
  imports: [CommonModule, RouterModule, MatToolbarModule, MatIconModule, MatButtonModule, NgOptimizedImage],
  exports: [HeaderComponent,FooterComponent],
})
export class CoreModule {}
