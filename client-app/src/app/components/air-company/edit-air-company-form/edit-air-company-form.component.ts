import { HttpErrorResponse } from '@angular/common/http';
import { Location } from './../../../model/location';
import { LocationService } from './../../../services/location-service';
import { Subject } from 'rxjs';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AirCompany } from './../../../model/air-company/air-company';
import { Component, OnInit, Input } from '@angular/core';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';

@Component({
  selector: 'app-edit-air-company-form',
  templateUrl: './edit-air-company-form.component.html',
  styleUrls: ['./edit-air-company-form.component.css',  '../../../shared/css/inputField.css']
})
export class EditAirCompanyFormComponent implements OnInit {
  public onClose: Subject<AirCompany>;
  errorMessage: String = '';
  editForm: FormGroup;
  @Input() airCompany: AirCompany = new AirCompany();
  @Input() isAddForm = false;

  constructor(public modalRef: BsModalRef, private formBuilder: FormBuilder,
    private airCompanyService: AirCompanyService, private locationService: LocationService ) { }

  ngOnInit() {
    this.onClose = new Subject();
    this.editForm = this.formBuilder.group({
      name: [this.airCompany.name, [Validators.required]],
      city: [this.airCompany.location.city, [Validators.required]],
      country: [this.airCompany.location.country, [Validators.required]],
      address: [this.airCompany.location.address, [Validators.required]],
      description: [this.airCompany.description]
    });
  }

  addressChanged(value): boolean {
    if (this.isAddForm) {
      return true;
    }
    return value.address !== this.airCompany.location.address || value.city !== this.airCompany.location.city
    || value.country !== this.airCompany.location.country;
  }

  onSubmit() {
    const value = this.editForm.value;
    value.location = new Location();
    if ( this.addressChanged(value) ) {
      value.location.address = value.address;
      value.location.city = value.city;
      value.location.country = value.country;
      this.locationService.decode(value.address + ',+' + value.city + ',+' + value.conutry).subscribe(
        (data) => {
          value.location.lat = data.results[0].geometry.location.lat;
          value.location.lon = data.results[0].geometry.location.lng;
          this.submit(value);
        },
        (error) => {
          console.log(error);
          value.location.lat = 0;
          value.location.lon = 0;
          this.submit(value);
        }
      );
    } else {
      // ovde ce uci samo edit, pa ako vec nisu menjali nista, da ne trazimo opet od googla koordinate
      value.location = this.airCompany.location;
      this.submit(value);
    }
  }

  submit(value) {
    if (this.isAddForm) {
      this.add(value);
    } else {
      this.edit(value);
    }
  }

  add(value) {
    this.airCompanyService.add(value).subscribe(data => {
        this.onClose.next(data);
        this.modalRef.hide();
      },
      (err: HttpErrorResponse) => {
        console.log(err);
        // interceptor je hendlovao ove zahteve
        if (err.status === 401 || err.status === 403 || err.status === 404) {
          this.modalRef.hide();
        }
        this.errorMessage = err.error.details;
      }
    );
  }

  edit(value) {
    value.id = this.airCompany.id;
    this.airCompanyService.edit(value).subscribe(
      data => {
        this.onClose.next(data);
        this.modalRef.hide();
      },
      error => {
        // interceptor je hendlovao ove zahteve
        if (error.status === 401 || error.status === 403 || error.status === 404) {
          this.modalRef.hide();
        }
        this.errorMessage = error.error.details;
      }
    );
  }

}
