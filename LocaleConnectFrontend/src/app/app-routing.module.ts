import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LandingPageComponent} from "./pages/landing-page/landing-page.component";
import {ItineraryComponent} from "./pages/itinerary/itinerary.component";
import {FeedComponent} from "./pages/feed/feed.component";

const routes: Routes = [
  {
    path: '',
    component:LandingPageComponent
  },{
  path: 'pages/itineraries',
    component: ItineraryComponent
  },
  {
    path: 'pages/home',
    component: FeedComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
