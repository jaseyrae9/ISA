import { Component, OnInit } from '@angular/core';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { DataService } from 'src/app/shared/services/data.service';


@Component({
  selector: 'app-all-cars-companies-page',
  templateUrl: './all-cars-companies-page.component.html',
  styleUrls: ['./all-cars-companies-page.component.css', '../../shared/css/inputField.css']
})
export class AllCarsCompaniesPageComponent implements OnInit {
  companies: RentACarCompany[];

  constructor(private rentACarCompanyService: RentACarCompanyService, private dataService : DataService) { }
 
  ngOnInit() {
    this.rentACarCompanyService.getAll().subscribe(data => {
      this.companies = data;
    });
  }

  carCompanyCreated(carCompany: RentACarCompany){
    this.companies.push(carCompany);
    this.dataService.changeCarCompany(carCompany);
  }

}
