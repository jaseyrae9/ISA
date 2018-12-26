import { Component, OnInit, Input } from '@angular/core';
import { AirCompany } from '../../model/air-company/air-company';

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
