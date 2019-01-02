import { Component, OnInit } from '@angular/core';
import { AirCompany } from 'src/app/model/air-company/air-company';
import { ActivatedRoute } from '@angular/router';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';

@Component({
  selector: 'app-air-company-page',
  templateUrl: './air-company-page.component.html',
  styleUrls: ['./air-company-page.component.css']
})
export class AirCompanyPageComponent implements OnInit {
  airCompany: AirCompany = new AirCompany();

  constructor(private route: ActivatedRoute, private airCompanyService: AirCompanyService) { }

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    this.airCompanyService.get(id).subscribe(
      (data) => {
        this.airCompany = data;
      }
    );
  }

}
