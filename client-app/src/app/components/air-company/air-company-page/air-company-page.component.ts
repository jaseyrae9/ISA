import { Component, OnInit } from '@angular/core';
import { AirCompany } from 'src/app/model/air-company/air-company';
import { ActivatedRoute } from '@angular/router';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { Role } from 'src/app/model/role';

@Component({
  selector: 'app-air-company-page',
  templateUrl: './air-company-page.component.html',
  styleUrls: ['./air-company-page.component.css', '../../../shared/css/inputField.css']
})
export class AirCompanyPageComponent implements OnInit {
  airCompany: AirCompany = new AirCompany();
  forEditing: AirCompany = new AirCompany();

  constructor(private route: ActivatedRoute, private airCompanyService: AirCompanyService, public tokenService: TokenStorageService) { }

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    this.airCompanyService.get(id).subscribe(
      (data) => {
        this.airCompany = data;
        this.forEditing = new AirCompany(data.id, data.name, data.description);
      }
    );
   }

  airCompanyEdited(data) {
    this.airCompany.id = data.id;
    this.airCompany.name = data.name;
    this.airCompany.description = data.description;
    this.forEditing = new AirCompany(data.id, data.name, data.description);
  }

}
