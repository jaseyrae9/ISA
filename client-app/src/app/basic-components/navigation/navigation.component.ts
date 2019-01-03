import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';
import { Hotel } from 'src/app/model/hotel/hotel';
import { HotelService } from 'src/app/services/hotel/hotel.service'
import { AirCompany } from 'src/app/model/air-company/air-company';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { DataService } from 'src/app/shared/services/data.service';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent implements OnInit {
  public loggedIn: Boolean;
  public carcompanies: RentACarCompany[]; 
  public hotels: Hotel[];
  public aircompanies: AirCompany[];

  constructor(private httpClient: HttpClient,
     public tokenService: TokenStorageService,
     private hotelService: HotelService,
     private airCompanyService : AirCompanyService,
     private carCompanyService : RentACarCompanyService,
     private dataService : DataService) {
    this.loggedIn = this.tokenService.loggedIn();
    this.tokenService.logggedInEmitter.subscribe(loggedIn => {
      this.loggedIn = loggedIn;
    });
  }

  ngOnInit() {
    this.hotelService.getAll().subscribe(data => {
      this.hotels = data;
    });

    this.airCompanyService.getAll().subscribe(data => {
      this.aircompanies = data;
    });
    this.carCompanyService.getAll().subscribe(data => {
      this.carcompanies = data;
    });
  }

}
