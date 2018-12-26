import { Component, OnInit} from '@angular/core';
import { AirCompany } from 'src/app/model/air-company/air-company';

@Component({
  selector: 'app-new-company-form',
  templateUrl: './new-company-form.component.html',
  styleUrls: ['../../shared/css/inputField.css']
})
export class NewCompanyFormComponent implements OnInit {
  avioCompany: AirCompany;

  constructor() { }

  ngOnInit() {}
}
