import { DataService } from './../../observables/data.service';
import { Component, OnInit } from '@angular/core';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { Role } from 'src/app/model/role';
import { TokenStorageService } from 'src/app/auth/token-storage.service';

@Component({
  selector: 'app-all-cars-companies-page',
  templateUrl: './all-cars-companies-page.component.html',
  styleUrls: ['./all-cars-companies-page.component.css', '../../shared/css/inputField.css']
})

export class AllCarsCompaniesPageComponent implements OnInit {
  companies: RentACarCompany[];
  roles: Role[];

  constructor(private rentACarCompanyService: RentACarCompanyService, private dataService: DataService,public tokenService: TokenStorageService) {
    
  }

  ngOnInit() {
    this.rentACarCompanyService.getAll().subscribe(data => {
      this.companies = data;
    });
    this.roles = this.tokenService.getRoles();
    this.tokenService.rolesEmitter.subscribe(roles => this.roles = roles);
  }

  carCompanyCreated(carCompany: RentACarCompany) {
    this.companies.push(carCompany);
    this.dataService.changeCarCompany(carCompany);
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
