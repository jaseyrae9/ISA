import { CarsRoomsFastComponent } from './components/shared/components/cars-rooms-fast/cars-rooms-fast.component';
import { TripInvitesComponent } from './components/reservations-history/trip-invites/trip-invites.component';
import { ReserveFlightFormComponent } from './components/air-company/flight/reserve-flight-form/reserve-flight-form.component';
import { AllReservationsComponent } from './components/reservations-history/all-reservations/all-reservations.component';
import { FlightPageComponent } from './components/air-company/flight/flight-page/flight-page.component';
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
import { FriendsPageComponent } from './components/user/friends/friends-page/friends-page.component';
import { AirCompanyPageComponent } from './components/air-company/air-company-page/air-company-page.component';
import { HotelPageComponent } from './components/hotel/hotel-page/hotel-page.component';
import { CarCompanyPageComponent } from './components/rent-a-car-company/car-company-page/car-company-page.component';
import { ErrorPageComponent } from './pages/error-page/error-page.component';
import { RoleGuardService } from 'src/app/auth/role-guard.service';

const routes: Routes = [
  { path: '', component: HomePageComponent },
  { path: 'aircompanies', component: AllAirCompaniesPageComponent },
  { path: 'aircompany/:id', component: AirCompanyPageComponent },
  { path: 'flight/:id', component: FlightPageComponent },
  { path: 'find-flight', component: FlightSearchPageComponent },
  { path: 'hotels', component: AllHotelsPageComponent },
  { path: 'hotel/:id', component: HotelPageComponent },
  { path: 'find-room', component: RoomSearchPageComponent },
  { path: 'rent-a-car-companies', component: AllCarsCompaniesPageComponent },
  { path: 'rent-a-car-company/:id', component: CarCompanyPageComponent },
  { path: 'find-car', component: CarSearchPageComponent },
  { path: 'cars-and-hotels/:city/:date/:ticketCount', component: CarsRoomsFastComponent},
  { path: 'reserve-flight/:id', component: ReserveFlightFormComponent ,
    canActivate: [RoleGuardService],
    data: {
      expectedRoles: ['CUSTOMER']
    }
  },
  { path: 'history', component: AllReservationsComponent  ,
    canActivate: [RoleGuardService],
    data: {
      expectedRoles: ['CUSTOMER']
    }
  },
  { path: 'invites', component: TripInvitesComponent  ,
    canActivate: [RoleGuardService],
    data: {
      expectedRoles: ['CUSTOMER']
    }
  },
  {
    path: 'profile', component: ProfileComponent,
    canActivate: [RoleGuardService],
    data: {
      expectedRoles: ['CUSTOMER', 'AIRADMIN', 'HOTELADMIN', 'CARADMIN', 'SYS']
    }
  },
  {
    path: 'friends', component: FriendsPageComponent,
    canActivate: [RoleGuardService],
    data: {
      expectedRoles: ['CUSTOMER']
    }
  },
  { path: 'error/:code', component: ErrorPageComponent },
  { path: '**', redirectTo: '' }
];

export const RoutingModule: ModuleWithProviders = RouterModule.forRoot(routes, {
  scrollPositionRestoration: 'enabled',
});
