import { DataService } from './../../observables/data.service';
import { Component, OnInit } from '@angular/core';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { AirCompany } from 'src/app/model/air-company/air-company';

@Component({
  selector: 'app-all-air-companies-page',
  templateUrl: './all-air-companies-page.component.html',
  styleUrls: ['./all-air-companies-page.component.css', '../../shared/css/inputField.css']
})
export class AllAirCompaniesPageComponent implements OnInit {
  companies: AirCompany[];

  constructor(private airCompanyService: AirCompanyService, private dataService: DataService) { }

  ngOnInit() {
    this.airCompanyService.getAll().subscribe(data => {
      this.companies = data;
    });
  }

  airCompanyCreated(airCompany: AirCompany) {
    this.companies.push(airCompany);
    this.dataService.changeAirCompany(airCompany);
  }
}
