import { AirCompany } from 'src/app/model/air-company/air-company';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-company-basic-details',
  templateUrl: './company-basic-details.component.html',
  styleUrls: ['./company-basic-details.component.css']
})
export class CompanyBasicDetailsComponent implements OnInit {
  @Input() company: AirCompany;

  constructor() { }

  ngOnInit() {
  }

}
