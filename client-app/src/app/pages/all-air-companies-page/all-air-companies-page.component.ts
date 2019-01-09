import { DataService } from './../../observables/data.service';
import { Component, OnInit } from '@angular/core';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { AirCompany } from 'src/app/model/air-company/air-company';
import * as decode from 'jwt-decode';
import { TokenPayload } from 'src/app/model/token-payload';

@Component({
  selector: 'app-all-air-companies-page',
  templateUrl: './all-air-companies-page.component.html',
  styleUrls: ['./all-air-companies-page.component.css', '../../shared/css/inputField.css']
})
export class AllAirCompaniesPageComponent implements OnInit {
  companies: AirCompany[];
  tokenPayload : TokenPayload;

  constructor(private airCompanyService: AirCompanyService, private dataService: DataService) {
   }

  ngOnInit() {
    this.airCompanyService.getAll().subscribe(data => {
      this.companies = data;
    });
    const token =  sessionStorage.getItem('AuthToken');
    // decode the token to get its payload
    const tokenPayload : TokenPayload = decode(token);
    this.tokenPayload = tokenPayload;
  
  }

  airCompanyCreated(airCompany: AirCompany) {
    this.companies.push(airCompany);
    this.dataService.changeAirCompany(airCompany);
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
