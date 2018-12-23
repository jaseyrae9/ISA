import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { NavigationComponent } from './basic-components/navigation/navigation.component';
import { BannerComponent } from './basic-components/banner/banner.component';
import { FooterComponent } from './basic-components/footer/footer.component';
import { FlightSearchPageComponent } from './pages/flight-search-page/flight-search-page.component';
import { AllAvioCompaniesPageComponent } from './pages/all-avio-companies-page/all-avio-companies-page.component';
import { RoomSearchPageComponent } from './pages/room-search-page/room-search-page.component';
import { AllHotelsPageComponent } from './pages/all-hotels-page/all-hotels-page.component';
import { AllCarsCompaniesPageComponent } from './pages/all-cars-companies-page/all-cars-companies-page.component';
import { CarSearchPageComponent } from './pages/car-search-page/car-search-page.component';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { RoutingModule } from './app-routing.module';
import { HotelService } from './services/hotel/hotel.service';
import { ProfileComponent } from './user/profile/profile.component';
import { ChangePasswordFormComponent } from './user/change-password-form/change-password-form.component';
import { LoginFormComponent } from './user/login-form/login-form.component';
import { RegisterFormComponent } from './user/register-form/register-form.component';
import { EditProfileFormComponent } from './user/edit-profile-form/edit-profile-form.component';
import { UserService } from './services/user/user.service';

@NgModule({
  declarations: [
    AppComponent,
    NavigationComponent,
    BannerComponent,
    FooterComponent,
    LoginFormComponent,
    RegisterFormComponent,
    HomePageComponent,
    AllAvioCompaniesPageComponent,
    AllHotelsPageComponent,
    AllCarsCompaniesPageComponent,
    FlightSearchPageComponent,
    RoomSearchPageComponent,
    CarSearchPageComponent,
    ProfileComponent,
    ChangePasswordFormComponent,
    EditProfileFormComponent
  ],
  imports: [
    BrowserModule, RoutingModule, HttpClientModule
  ],
  providers: [HotelService, UserService],
  bootstrap: [AppComponent]
})
export class AppModule { }
