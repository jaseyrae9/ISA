import { DataService } from './../../../observables/data.service';
import { Component, OnInit } from '@angular/core';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';
import { User } from 'src/app/model/users/user';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { HttpErrorResponse } from '@angular/common/http';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-add-car-admin',
  templateUrl: './add-car-admin.component.html',
  styleUrls: ['./add-car-admin.component.css',  '../../../shared/css/inputField.css']
})
export class AddCarAdminComponent implements OnInit {
  errorMessage: String = '';

  carcompanies: RentACarCompany[];
  private user: User = new User();
  newCarCompanyAdminForm: FormGroup;

  constructor(private rentACarCompanyService: RentACarCompanyService,
    private dataService: DataService,
    public modalRef: BsModalRef,
    private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.rentACarCompanyService.getAllCompanies().subscribe(data => {
      this.carcompanies = data;
    });

    this.dataService.currentCarCompany.subscribe(car => {
      if (car.id != null) {
        this.carcompanies.push(car);
      }
    });

    this.newCarCompanyAdminForm = this.formBuilder.group({
      email: [this.user.email, [Validators.required, Validators.email]],
      firstName: [this.user.firstName, [Validators.required]],
      lastName: [this.user.lastName, [Validators.required]],
      address: [this.user.address, [Validators.required]],
      phoneNumber: [this.user.phoneNumber, [Validators.required]],
      carcompany: ['', [Validators.required]]
    });
  }

  onCarCompanyAdminAdd() {
    console.log(this.newCarCompanyAdminForm.value);

    this.user.password = 'changeme';
    this.user.email = this.newCarCompanyAdminForm.value.email;
    this.user.firstName = this.newCarCompanyAdminForm.value.firstName;
    this.user.lastName = this.newCarCompanyAdminForm.value.lastName;
    this.user.address = this.newCarCompanyAdminForm.value.address;
    this.user.phoneNumber = this.newCarCompanyAdminForm.value.phoneNumber;


    this.rentACarCompanyService.addAdmin(this.user, this.newCarCompanyAdminForm.value.carcompany).subscribe(
      data => {
        console.log(data);
        this.modalRef.hide();
      },
      (err: HttpErrorResponse) => {
        // interceptor je hendlovao ove zahteve
        if (err.status === 401 || err.status === 403 || err.status === 404) {
          this.modalRef.hide();
        }
        this.errorMessage = err.error.details;
      }
    );

  }

}
