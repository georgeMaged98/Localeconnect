import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LandingPageComponent} from "./pages/landing-page/landing-page.component";
import {ItineraryComponent} from "./pages/itinerary/itinerary.component";
import {FeedComponent} from "./pages/feed/feed.component";
import {MeetupComponent} from "./pages/meetup/meetup.component";
import {GuideComponent} from "./pages/guide/guide.component";
import {authGuard} from "./service/auth-guard";
//TODO: uncomment guard
const routes: Routes = [
  {
    path: '',
    component:LandingPageComponent
  },{
  path: 'pages/itineraries',
    component: ItineraryComponent,

//    canActivate:[authGuard]
  },
  {
    path: 'pages/home',
    component: FeedComponent,
   // canActivate:[authGuard]
  },
  {
    path: 'pages/meetups',
    component: MeetupComponent,
    // canActivate:[authGuard]
  },

  {
    path: 'pages/guides',
    component: GuideComponent,
    // canActivate:[authGuard]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
