import { DataService } from './../../../observables/data.service';
import { Component, OnInit } from '@angular/core';
import { AirCompany } from 'src/app/model/air-company/air-company';
import { User } from 'src/app/model/users/user';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-add-air-company-admin',
  templateUrl: './add-air-company-admin.component.html',
  styleUrls: ['./add-air-company-admin.component.css',  './../../../shared/css/inputField.css']
})
export class AddAirCompanyAdminComponent implements OnInit {
  errorMessage: String = '';

  aircompanies: AirCompany[];
  private user: User = new User();
  newAirCompanyAdminForm: FormGroup;

  constructor(private airCompanyService: AirCompanyService,
    private dataService: DataService,
    public modalRef: BsModalRef,
    private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.airCompanyService.getAll().subscribe(data => {
      this.aircompanies = data;
    });

    // Kada se doda da se ne mora refresh
    this.dataService.currentAirCompany.subscribe(aircompany => {
      if (aircompany.id != null) {
        this.aircompanies.push(aircompany);
      }
    });

    this.newAirCompanyAdminForm = this.formBuilder.group({
      email: [this.user.email, [Validators.required]],
      firstName: [this.user.firstName, [Validators.required]],
      lastName: [this.user.lastName, [Validators.required]],
      address: [this.user.address, [Validators.required]],
      phoneNumber: [this.user.phoneNumber, [Validators.required]],
      aircompany: ['', [Validators.required]]
    });

  }

  onAirCompanyAdminAdd() {
    console.log(this.newAirCompanyAdminForm.value);

    this.user.password = 'changeme';
    this.user.email = this.newAirCompanyAdminForm.value.email;
    this.user.firstName = this.newAirCompanyAdminForm.value.firstName;
    this.user.lastName = this.newAirCompanyAdminForm.value.lastName;
    this.user.address = this.newAirCompanyAdminForm.value.address;
    this.user.phoneNumber = this.newAirCompanyAdminForm.value.phoneNumber;

    this.airCompanyService.addAdmin(this.user, this.newAirCompanyAdminForm.value.aircompany).subscribe(
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
