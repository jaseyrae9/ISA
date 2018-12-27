import { Component, OnInit } from '@angular/core';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';

@Component({
  selector: 'app-new-car-company-form',
  templateUrl: './new-car-company-form.component.html',
  styleUrls: ['./new-car-company-form.component.css']
})
export class NewCarCompanyFormComponent implements OnInit {
  carCompany: RentACarCompany;

  constructor() { }

  ngOnInit() {
  }

}
