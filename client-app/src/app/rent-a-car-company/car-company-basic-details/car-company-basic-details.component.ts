import { Component, OnInit, Input } from '@angular/core';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';

@Component({
  selector: 'app-car-company-basic-details',
  templateUrl: './car-company-basic-details.component.html',
  styleUrls: ['./car-company-basic-details.component.css']
})
export class CarCompanyBasicDetailsComponent implements OnInit {
  @Input() carCompany: RentACarCompany;
  
  constructor() { }

  ngOnInit() {
  }

}
