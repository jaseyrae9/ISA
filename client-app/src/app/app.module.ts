import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule} from '@angular/forms';
import { HTTP_INTERCEPTORS } from '@angular/common/http';

import { JwtInterceptor } from './auth/auth-interceptor';

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
import { NewCompanyFormComponent } from './air-company/new-company-form/new-company-form.component';
import { CompanyBasicDetailsComponent } from './air-company/company-basic-details/company-basic-details.component';
import { AirCompanyService } from './services/air-company/air-company.service';
import { NewHotelFormComponent } from './hotel/new-hotel-form/new-hotel-form.component';
import { HotelBasicDetailsComponent } from './hotel/hotel-basic-details/hotel-basic-details.component';
import { NewCarCompanyFormComponent } from './rent-a-car-company/new-car-company-form/new-car-company-form.component';
import { CarCompanyBasicDetailsComponent} from './rent-a-car-company/car-company-basic-details/car-company-basic-details.component';
import { AddHotelAdminComponent } from './hotel/add-hotel-admin/add-hotel-admin.component';
import { FriendsPageComponent } from './user/friends/friends-page/friends-page.component';
import { FriendRequestComponent } from './user/friends/friend-request/friend-request.component';
import { AddAirCompanyAdminComponent } from './air-company/add-air-company-admin/add-air-company-admin.component';

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
    EditProfileFormComponent,
    NewCompanyFormComponent,
    NewCarCompanyFormComponent,
    CarCompanyBasicDetailsComponent,
    CompanyBasicDetailsComponent,
    CarCompanyBasicDetailsComponent,
    NewCarCompanyFormComponent,
    HotelBasicDetailsComponent,
    NewHotelFormComponent,
    NewHotelFormComponent,
    HotelBasicDetailsComponent,
    AddHotelAdminComponent,
    FriendsPageComponent,
    FriendRequestComponent,
    AddAirCompanyAdminComponent
  ],
  imports: [
    BrowserModule, RoutingModule, HttpClientModule, FormsModule
  ],
  providers: [HotelService, AirCompanyService, UserService,
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
