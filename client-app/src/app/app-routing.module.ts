import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ModuleWithProviders } from '@angular/core';


import { FlightSearchPageComponent } from './pages/flight-search-page/flight-search-page.component';
import { AllAirCompaniesPageComponent } from './pages/all-air-companies-page/all-air-companies-page.component';
import { RoomSearchPageComponent } from './pages/room-search-page/room-search-page.component';
import { AllHotelsPageComponent } from './pages/all-hotels-page/all-hotels-page.component';
import { AllCarsCompaniesPageComponent } from './pages/all-cars-companies-page/all-cars-companies-page.component';
import { CarSearchPageComponent } from './pages/car-search-page/car-search-page.component';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { ProfileComponent } from './components/user/profile/profile.component';
import { LoginFormComponent} from './components/user/login-form/login-form.component';
import { FriendsPageComponent } from './components/user/friends/friends-page/friends-page.component';
import { AirCompanyPageComponent } from './components/air-company/air-company-page/air-company-page.component';
import { HotelPageComponent } from './components/hotel/hotel-page/hotel-page.component';
import { CarCompanyPageComponent } from './components/rent-a-car-company/car-company-page/car-company-page.component';

const routes: Routes = [
  { path: '', component: HomePageComponent},
  { path: 'aircompanies', component: AllAirCompaniesPageComponent},
  { path: 'aircompany/:id', component: AirCompanyPageComponent},
  { path: 'find-flight', component: FlightSearchPageComponent },
  { path: 'hotels', component: AllHotelsPageComponent },
  { path: 'hotel/:id', component: HotelPageComponent },
  { path: 'find-room', component: RoomSearchPageComponent },
  { path: 'rent-a-car-companies', component: AllCarsCompaniesPageComponent },
  { path: 'rent-a-car-company/:id', component: CarCompanyPageComponent},
  { path: 'find-car', component: CarSearchPageComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'friends', component: FriendsPageComponent },
  { path: 'login', component: LoginFormComponent }
];

export const RoutingModule: ModuleWithProviders = RouterModule.forRoot(routes, {
  scrollPositionRestoration: 'enabled',
});
