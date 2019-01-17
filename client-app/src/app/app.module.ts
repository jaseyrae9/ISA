import { ErrorInterceptor } from './auth/response-interceptor';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule} from '@angular/forms';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgxNotificationComponent } from 'ngx-notification';
import { ModalModule } from 'ngx-bootstrap/modal';

import { JwtInterceptor } from './auth/auth-interceptor';

import { AppComponent } from './app.component';
import { NavigationComponent } from './components/basic-components/navigation/navigation.component';
import { BannerComponent } from './components/basic-components/banner/banner.component';
import { FooterComponent } from './components/basic-components/footer/footer.component';
import { FlightSearchPageComponent } from './pages/flight-search-page/flight-search-page.component';
import { AllAirCompaniesPageComponent } from './pages/all-air-companies-page/all-air-companies-page.component';
import { RoomSearchPageComponent } from './pages/room-search-page/room-search-page.component';
import { AllHotelsPageComponent } from './pages/all-hotels-page/all-hotels-page.component';
import { AllCarsCompaniesPageComponent } from './pages/all-cars-companies-page/all-cars-companies-page.component';
import { CarSearchPageComponent } from './pages/car-search-page/car-search-page.component';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { RoutingModule } from './app-routing.module';
import { HotelService } from './services/hotel/hotel.service';
import { ProfileComponent } from './components/user/profile/profile.component';
import { ChangePasswordFormComponent } from './components/user/change-password-form/change-password-form.component';
import { LoginFormComponent } from './components/user/login-form/login-form.component';
import { RegisterFormComponent } from './components/user/register-form/register-form.component';
import { EditProfileFormComponent } from './components/user/edit-profile-form/edit-profile-form.component';
import { UserService } from './services/user/user.service';
import { NewAirCompanyFormComponent } from './components/air-company/new-air-company-form/new-air-company-form.component';
import { CompanyBasicDetailsComponent } from './components/air-company/company-basic-details/company-basic-details.component';
import { AirCompanyService } from './services/air-company/air-company.service';
import { NewHotelFormComponent } from './components/hotel/new-hotel-form/new-hotel-form.component';
import { HotelBasicDetailsComponent } from './components/hotel/hotel-basic-details/hotel-basic-details.component';
import { NewCarCompanyFormComponent } from './components/rent-a-car-company/new-car-company-form/new-car-company-form.component';
// tslint:disable-next-line:max-line-length
import { CarCompanyBasicDetailsComponent} from './components/rent-a-car-company/car-company-basic-details/car-company-basic-details.component';
import { AddHotelAdminComponent } from './components/hotel/add-hotel-admin/add-hotel-admin.component';
import { FriendsPageComponent } from './components/user/friends/friends-page/friends-page.component';
import { FriendshipDisplayComponent } from './components/user/friends/friendship-display/friendship-display.component';
import { AddAirCompanyAdminComponent } from './components/air-company/add-air-company-admin/add-air-company-admin.component';
import { FriendRequestsPageComponent } from './components/user/friends/friend-requests-page/friend-requests-page.component';
import { FriendshipsPageComponent } from './components/user/friends/friendships-page/friendships-page.component';
import { FindFriendsPageComponent } from './components/user/friends/find-friends-page/find-friends-page.component';
import { AdditionalServiceComponent } from './components/shared/components/additional-service/additional-service.component';
// tslint:disable-next-line:max-line-length
import { AdditionalServicesTableComponent } from './components/shared/components/additional-services-table/additional-services-table.component';
import { AirCompanyPageComponent } from './components/air-company/air-company-page/air-company-page.component';
import { FightBasicInfoComponent } from './components/air-company/fight-basic-info/fight-basic-info.component';
import { AddCarAdminComponent } from './components/rent-a-car-company/add-car-admin/add-car-admin.component';
import { DataService } from './observables/data.service';
import { HotelPageComponent } from './components/hotel/hotel-page/hotel-page.component';
import { CarCompanyPageComponent } from './components/rent-a-car-company/car-company-page/car-company-page.component';
import { CarBasicInfoComponent } from './components/rent-a-car-company/car-basic-info/car-basic-info.component';
import { NewCarFormComponent } from './components/rent-a-car-company/new-car-form/new-car-form.component';
import { EditHotelFormComponent } from './components/hotel/edit-hotel-form/edit-hotel-form.component';
import { EditCarCompanyFormComponent } from './components/rent-a-car-company/edit-car-company-form/edit-car-company-form.component';
import { EditAirCompanyFormComponent } from './components/air-company/edit-air-company-form/edit-air-company-form.component';
import { EditCarFormComponent } from './components/rent-a-car-company/edit-car-form/edit-car-form.component';
import { RoomBasicInfoComponent } from './components/hotel/room-basic-info/room-basic-info.component';
import { NewRoomFormComponent } from './components/hotel/new-room-form/new-room-form.component';
import { ErrorPageComponent } from './pages/error-page/error-page.component';
import { RoleGuardService } from 'src/app/auth/role-guard.service';
import { EditRoomFormComponent } from './components/hotel/edit-room-form/edit-room-form.component';
import { NewServiceFormComponent } from './components/hotel/new-service-form/new-service-form.component';
import { BranchOfficeBasicDetailsComponent } from './components/rent-a-car-company/branch-office-basic-details/branch-office-basic-details.component';
import { NewBranchOfficeFormComponent } from './components/rent-a-car-company/new-branch-office-form/new-branch-office-form.component';

@NgModule({
  declarations: [
    AppComponent,
    NgxNotificationComponent,
    NavigationComponent,
    BannerComponent,
    FooterComponent,
    LoginFormComponent,
    RegisterFormComponent,
    HomePageComponent,
    AllAirCompaniesPageComponent,
    AllHotelsPageComponent,
    AllCarsCompaniesPageComponent,
    FlightSearchPageComponent,
    RoomSearchPageComponent,
    CarSearchPageComponent,
    ProfileComponent,
    ChangePasswordFormComponent,
    EditProfileFormComponent,
    NewAirCompanyFormComponent,
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
    FriendshipDisplayComponent,
    AddAirCompanyAdminComponent,
    FriendRequestsPageComponent,
    FriendshipsPageComponent,
    FindFriendsPageComponent,
    AdditionalServiceComponent,
    AdditionalServicesTableComponent,
    AirCompanyPageComponent,
    FightBasicInfoComponent,
    AddCarAdminComponent,
    HotelPageComponent,
    CarCompanyPageComponent,
    CarBasicInfoComponent,
    NewCarFormComponent,
    EditHotelFormComponent,
    EditCarCompanyFormComponent,
    EditAirCompanyFormComponent,
    EditCarFormComponent,
    RoomBasicInfoComponent,
    NewRoomFormComponent,
    ErrorPageComponent,
    EditRoomFormComponent,
    NewServiceFormComponent,
    BranchOfficeBasicDetailsComponent,
    NewBranchOfficeFormComponent
  ],
  imports: [
    BrowserModule, RoutingModule, HttpClientModule, FormsModule, ModalModule.forRoot()
  ],
  providers: [HotelService, AirCompanyService, UserService, DataService, RoleGuardService,
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }
  ],
  bootstrap: [AppComponent],
  entryComponents: [EditCarFormComponent, EditRoomFormComponent]
})
export class AppModule { }
