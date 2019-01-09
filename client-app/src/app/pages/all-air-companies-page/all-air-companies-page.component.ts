import { DataService } from './../../observables/data.service';
import { Component, OnInit } from '@angular/core';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { AirCompany } from 'src/app/model/air-company/air-company';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { Role } from 'src/app/model/role';

@Component({
  selector: 'app-all-air-companies-page',
  templateUrl: './all-air-companies-page.component.html',
  styleUrls: ['./all-air-companies-page.component.css', '../../shared/css/inputField.css']
})
export class AllAirCompaniesPageComponent implements OnInit {
  companies: AirCompany[];
  roles: Role[];

  constructor(private airCompanyService: AirCompanyService, private dataService: DataService, public tokenService: TokenStorageService) {
   }

  ngOnInit() {
    this.airCompanyService.getAll().subscribe(data => {
      this.companies = data;
    });
    this.roles = this.tokenService.getRoles();
    this.tokenService.rolesEmitter.subscribe(roles => this.roles = roles);
  
  }

  airCompanyCreated(airCompany: AirCompany) {
    this.companies.push(airCompany);
    this.dataService.changeAirCompany(airCompany);
  }

  isSysAdmin() {
    if (this.roles != undefined) {
      for (let role of this.roles) {
        if (role.authority == 'ROLE_SYS') {
          return true;
        }
      }
      return false;
    }
    return false;
  }
}
