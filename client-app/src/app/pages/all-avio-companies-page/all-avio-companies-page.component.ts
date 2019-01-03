import { Component, OnInit } from '@angular/core';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { AirCompany } from 'src/app/model/air-company/air-company';
import { DataService } from 'src/app/shared/services/data.service';

@Component({
  selector: 'app-all-avio-companies-page',
  templateUrl: './all-avio-companies-page.component.html',
  styleUrls: ['./all-avio-companies-page.component.css', '../../shared/css/inputField.css']
})
export class AllAvioCompaniesPageComponent implements OnInit {
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
