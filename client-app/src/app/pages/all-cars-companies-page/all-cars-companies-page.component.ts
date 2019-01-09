import { DataService } from './../../observables/data.service';
import { Component, OnInit } from '@angular/core';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import * as decode from 'jwt-decode';
import { TokenPayload } from 'src/app/model/token-payload';

@Component({
  selector: 'app-all-cars-companies-page',
  templateUrl: './all-cars-companies-page.component.html',
  styleUrls: ['./all-cars-companies-page.component.css', '../../shared/css/inputField.css']
})

export class AllCarsCompaniesPageComponent implements OnInit {
  companies: RentACarCompany[];
  tokenPayload : TokenPayload;

  constructor(private rentACarCompanyService: RentACarCompanyService, private dataService: DataService) {  
   }

  ngOnInit() {
    this.rentACarCompanyService.getAll().subscribe(data => {
      this.companies = data;
    });
    const token =  sessionStorage.getItem('AuthToken');
    // decode the token to get its payload
    const tokenPayload : TokenPayload = decode(token);
    this.tokenPayload = tokenPayload;
  }

  carCompanyCreated(carCompany: RentACarCompany) {
    this.companies.push(carCompany);
    this.dataService.changeCarCompany(carCompany);
  }

  isSysAdmin()
  {
    for(let role of this.tokenPayload.roles)
    {
      if(role.authority == 'ROLE_SYS')
      {
        return true;
      }
    }
    return false;
  }

}
