import { ReservationsService } from './services/reservations.service';
import { ShoppingCartService } from './observables/shopping-cart.service';
import { LocationService } from './services/location-service';
import { ErrorInterceptor } from './auth/response-interceptor';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule} from '@angular/forms';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { ModalModule, BsModalRef } from 'ngx-bootstrap/modal';

import { JwtInterceptor } from './auth/auth-interceptor';
import { NgBootstrapFormValidationModule } from 'ng-bootstrap-form-validation';

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
import { FightBasicInfoComponent } from './components/air-company/flight/fight-basic-info/fight-basic-info.component';
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
// tslint:disable-next-line:max-line-length
import { BranchOfficeBasicDetailsComponent } from './components/rent-a-car-company/branch-office-basic-details/branch-office-basic-details.component';
import { NewBranchOfficeFormComponent } from './components/rent-a-car-company/new-branch-office-form/new-branch-office-form.component';
import { DestinationComponent } from './components/air-company/destination/destination.component';
import { DestinationFormComponent } from './components/air-company/destination-form/destination-form.component';
import { EditBranchOfficeFormComponent } from './components/rent-a-car-company/edit-branch-office-form/edit-branch-office-form.component';
import { EditServiceFormComponent } from './components/hotel/edit-service-form/edit-service-form.component';
// sortable
import { NgxSortableModule } from 'ngx-sortable';
// rating
import { RatingModule } from 'ngx-rating';
// datepicker
import { BsDatepickerModule} from 'ngx-bootstrap';
import { AirplaneDisplayComponent } from './components/air-company/airplane/airplane-display/airplane-display.component';
// tooltips
import { TooltipModule } from 'ngx-bootstrap/tooltip';
// timepicker
import { TimepickerModule } from 'ngx-bootstrap/timepicker';
import {DataTableModule} from 'angular-6-datatable';
import { AirplaneFormComponent } from './components/air-company/airplane/airplane-form/airplane-form.component';
import { MapComponent } from './components/shared/components/map/map.component';
import { AgmCoreModule } from '@agm/core';
import { RentFormComponent } from './components/rent-a-car-company/rent-form/rent-form.component';
import { Ng5SliderModule } from 'ng5-slider';
import { BookFormComponent } from './components/hotel/book-form/book-form.component';
import { BaggageFormComponent } from './components/air-company/baggage-form/baggage-form.component';
import { FlightDisplayComponent } from './components/air-company/flight/flight-display/flight-display.component';
import { FlightFormComponent } from './components/air-company/flight/flight-form/flight-form.component';
import { DatePipe } from '@angular/common';
import { FlightPageComponent } from './components/air-company/flight/flight-page/flight-page.component';
import { TicketsDisplayComponent } from './components/air-company/flight/tickets-display/tickets-display.component';
import { MonthlyIncomeChartComponent } from './components/shared/components/monthly-income-chart/monthly-income-chart.component';
import { WeeklyIncomeChartComponent } from './components/shared/components/weekly-income-chart/weekly-income-chart.component';
import { DailyIncomeChartComponent } from './components/shared/components/daily-income-chart/daily-income-chart.component';
// tslint:disable-next-line:max-line-length
import { ChangeTicketsPricesFormComponent } from './components/air-company/flight/change-tickets-prices-form/change-tickets-prices-form.component';
// tslint:disable-next-line:max-line-length
import { CreateFastReservationsFormComponent } from './components/air-company/flight/create-fast-reservations-form/create-fast-reservations-form.component';
import { FastTicketDisplayComponent } from './components/air-company/flight/fast-ticket-display/fast-ticket-display.component';
import { DisableSeatsFormComponent } from './components/air-company/flight/disable-seats-form/disable-seats-form.component';
import { AllReservationsComponent } from './components/reservations-history/all-reservations/all-reservations.component';
import { TicketsReservationComponent } from './components/reservations-history/tickets-reservation/tickets-reservation.component';
import { CarReservationComponent } from './components/reservations-history/car-reservation/car-reservation.component';
import { HotelReservationComponent } from './components/reservations-history/hotel-reservation/hotel-reservation.component';
import { ReservationComponent } from './components/reservations-history/reservation/reservation.component';
import { ReserveFlightFormComponent } from './components/air-company/flight/reserve-flight-form/reserve-flight-form.component';
import { AddSysAdminComponent } from './components/sys-admin/add-sys-admin/add-sys-admin.component';
import { ReservationInformationComponent } from './components/air-company/flight/reservation-information/reservation-information.component';
import { InviteAFriendComponent } from './components/air-company/flight/invite-a-friend/invite-a-friend.component';
import { ShoppingCartComponent } from './components/shared/components/shopping-cart/shopping-cart.component';
import { FastTicketFormComponent } from './components/rent-a-car-company/fast-ticket-form/fast-ticket-form.component';
// tslint:disable-next-line:max-line-length
import { FastReservationDisplayComponent } from './components/rent-a-car-company/fast-reservation-display/fast-reservation-display.component';
import { CarsRoomsFastComponent } from './components/shared/components/cars-rooms-fast/cars-rooms-fast.component';
import { FastRoomDisplayComponent } from './components/hotel/fast-room-display/fast-room-display.component';
import { MakeRoomFastComponent } from './components/hotel/make-room-fast/make-room-fast.component';
import { TripInvitesComponent } from './components/reservations-history/trip-invites/trip-invites.component';
import { AlertModule } from 'ngx-alerts';

@NgModule({
  declarations: [
    AppComponent,
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
    NewBranchOfficeFormComponent,
    DestinationComponent,
    DestinationFormComponent,
    EditBranchOfficeFormComponent,
    AirplaneDisplayComponent,
    AirplaneFormComponent,
    EditServiceFormComponent,
    MapComponent,
    RentFormComponent,
    BookFormComponent,
    BaggageFormComponent,
    FlightDisplayComponent,
    FlightFormComponent,
    FlightPageComponent,
    TicketsDisplayComponent,
    MonthlyIncomeChartComponent,
    WeeklyIncomeChartComponent,
    DailyIncomeChartComponent,
    ChangeTicketsPricesFormComponent,
    CreateFastReservationsFormComponent,
    FastTicketDisplayComponent,
    DisableSeatsFormComponent,
    AllReservationsComponent,
    TicketsReservationComponent,
    CarReservationComponent,
    HotelReservationComponent,
    ReservationComponent,
    ReserveFlightFormComponent,
    AddSysAdminComponent,
    ReservationInformationComponent,
    InviteAFriendComponent,
    ShoppingCartComponent,
    FastTicketFormComponent,
    FastReservationDisplayComponent,
    FastReservationDisplayComponent,
    CarsRoomsFastComponent,
    FastRoomDisplayComponent,
    MakeRoomFastComponent,
    TripInvitesComponent
  ],
  imports: [
    BrowserModule,
    RoutingModule,
    HttpClientModule,
    FormsModule,
    ModalModule.forRoot(),
    RatingModule,
    BsDatepickerModule.forRoot(),
    ReactiveFormsModule,
    NgBootstrapFormValidationModule.forRoot(),
    NgBootstrapFormValidationModule,
    TooltipModule.forRoot(),
    AgmCoreModule.forRoot({
      apiKey: 'AIzaSyDxMEmiWGtvuSRvzBShDgvPbWYQgo3GEHQ'
    }),
    Ng5SliderModule,
    NgxSortableModule,
    TimepickerModule.forRoot(),
    DataTableModule,
    AlertModule.forRoot({maxMessages: 5, timeout: 5000, position: 'right'})
  ],
  providers: [HotelService, AirCompanyService, UserService, DataService, RoleGuardService, LocationService,
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
    DatePipe, ShoppingCartService, ReservationsService
  ],
  bootstrap: [AppComponent],
  entryComponents: [
    EditCarFormComponent,
    EditRoomFormComponent,
    DestinationFormComponent,
    EditBranchOfficeFormComponent,
    EditAirCompanyFormComponent,
    EditProfileFormComponent,
    AirplaneFormComponent,
    EditServiceFormComponent,
    NewServiceFormComponent,
    NewCarFormComponent,
    NewBranchOfficeFormComponent,
    NewCarCompanyFormComponent,
    EditCarCompanyFormComponent,
    NewHotelFormComponent,
    EditHotelFormComponent,
    NewRoomFormComponent,
    BaggageFormComponent,
    FlightFormComponent,
    ChangeTicketsPricesFormComponent,
    CreateFastReservationsFormComponent,
    DisableSeatsFormComponent,
    AddAirCompanyAdminComponent,
    AddHotelAdminComponent,
    AddCarAdminComponent,
    AddSysAdminComponent,
    ReservationInformationComponent,
    InviteAFriendComponent,
    FastTicketFormComponent,
    FastTicketFormComponent,
    MakeRoomFastComponent
]
})
export class AppModule { }
