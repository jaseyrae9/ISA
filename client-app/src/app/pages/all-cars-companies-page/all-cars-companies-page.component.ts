import { DataService } from './../../observables/data.service';
import { Component, OnInit } from '@angular/core';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { NgxNotificationService } from 'ngx-notification';

@Component({
  selector: 'app-all-cars-companies-page',
  templateUrl: './all-cars-companies-page.component.html',
  styleUrls: ['./all-cars-companies-page.component.css', '../../shared/css/inputField.css']
})

export class AllCarsCompaniesPageComponent implements OnInit {
  companies: RentACarCompany[];

  constructor(private rentACarCompanyService: RentACarCompanyService, private dataService: DataService,
    public tokenService: TokenStorageService, public ngxNotificationService: NgxNotificationService) {
  }

  ngOnInit() {
    this.rentACarCompanyService.getAll().subscribe(data => {
      this.companies = data;
    });
  }

  carCompanyCreated(carCompany: RentACarCompany) {
    this.companies.push(carCompany);
    this.dataService.changeCarCompany(carCompany);
    this.ngxNotificationService.sendMessage(carCompany.name + ' is created!', 'dark', 'bottom-right' );
  }
}
