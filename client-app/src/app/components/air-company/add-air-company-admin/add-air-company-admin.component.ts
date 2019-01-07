import { DataService } from './../../../observables/data.service';
import { Component, OnInit, Input } from '@angular/core';
import { AirCompany } from 'src/app/model/air-company/air-company';
import { User } from 'src/app/model/users/user';
import { NgForm } from '@angular/forms';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';

@Component({
  selector: 'app-add-air-company-admin',
  templateUrl: './add-air-company-admin.component.html',
  styleUrls: ['./add-air-company-admin.component.css',  './../../../shared/css/inputField.css']
})
export class AddAirCompanyAdminComponent implements OnInit {
  aircompanies: AirCompany[];

  private user: User;

  constructor(private airCompanyService: AirCompanyService, private dataService: DataService) { }

  ngOnInit() {
    this.airCompanyService.getAll().subscribe(data => {
      this.aircompanies = data;
    });
    this.dataService.currentAirCompany.subscribe(aircompany => {
      if (aircompany.id != null) {
        this.aircompanies.push(aircompany);
      }
    });
  }

  onSubmit(form: NgForm) {
    console.log(form);

    // tslint:disable-next-line:max-line-length
    this.user = new User('changeme', form.value.firstName, form.value.lastName, form.value.email, form.value.phoneNumber, form.value.address);

    this.airCompanyService.addAdmin(this.user, form.value.aircompany).subscribe(
      data => {
        console.log(data);
      },
      error => {
        console.log(error);
      }
    );

  }

}
