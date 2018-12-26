import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ModuleWithProviders } from '@angular/core';


import { FlightSearchPageComponent } from './pages/flight-search-page/flight-search-page.component';
import { AllAvioCompaniesPageComponent } from './pages/all-avio-companies-page/all-avio-companies-page.component';
import { RoomSearchPageComponent } from './pages/room-search-page/room-search-page.component';
import { AllHotelsPageComponent } from './pages/all-hotels-page/all-hotels-page.component';
import { AllCarsCompaniesPageComponent } from './pages/all-cars-companies-page/all-cars-companies-page.component';
import { CarSearchPageComponent } from './pages/car-search-page/car-search-page.component';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { ProfileComponent } from './user/profile/profile.component';
import { LoginFormComponent} from './user/login-form/login-form.component';
import { FriendsComponent } from './user/friends/friends.component';

const routes: Routes = [
  { path: '', component: HomePageComponent},
  { path: 'aircompanies', component: AllAvioCompaniesPageComponent},
  { path: 'find-flight', component: FlightSearchPageComponent },
  { path: 'hotels', component: AllHotelsPageComponent },
  { path: 'find-room', component: RoomSearchPageComponent },
  { path: 'rent-a-car-companies', component: AllCarsCompaniesPageComponent },
  { path: 'find-car', component: CarSearchPageComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'friends', component: FriendsComponent },
  { path: 'login', component: LoginFormComponent }
];

export const RoutingModule: ModuleWithProviders = RouterModule.forRoot(routes, {
  scrollPositionRestoration: 'enabled',
});
