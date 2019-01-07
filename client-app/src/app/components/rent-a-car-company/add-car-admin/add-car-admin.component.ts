import { DataService } from './../../../observables/data.service';
import { Component, OnInit, Input } from '@angular/core';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';
import { User } from 'src/app/model/users/user';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-add-car-admin',
  templateUrl: './add-car-admin.component.html',
  styleUrls: ['./add-car-admin.component.css',  '../../../shared/css/inputField.css']
})
export class AddCarAdminComponent implements OnInit {
  carcompanies: RentACarCompany[];

  private user: User;

  constructor(private rentACarCompanyService: RentACarCompanyService, private dataService: DataService) { }

  ngOnInit() {
    this.rentACarCompanyService.getAll().subscribe(data => {
      this.carcompanies = data;
    });
    this.dataService.currentCarCompany.subscribe(car => {
      if (car.id != null) {
        this.carcompanies.push(car);
      }
    });
  }

  onSubmit(form: NgForm) {
    console.log(form);
    // tslint:disable-next-line:max-line-length
    this.user = new User('changeme', form.value.firstName, form.value.lastName, form.value.email, form.value.phoneNumber, form.value.address);


    this.rentACarCompanyService.addAdmin(this.user, form.value.carcompany).subscribe(
      data => {
        console.log(data);
      },
      error => {
        console.log(error);
      }
    );

  }

}
