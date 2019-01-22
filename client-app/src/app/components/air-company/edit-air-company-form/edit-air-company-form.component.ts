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
  @Input() airCompany: AirCompany;
  errorMessage: String = '';
  editForm: FormGroup;

  constructor(public modalRef: BsModalRef, private formBuilder: FormBuilder,
    private airCompanyService: AirCompanyService, private locationService: LocationService ) { }

  ngOnInit() {
    this.onClose = new Subject();
    this.editForm = this.formBuilder.group({
      name: [this.airCompany.name, [Validators.required]],
      address: [this.airCompany.location.address, [Validators.required]],
      description: [this.airCompany.description]
    });
  }

  onEditAirCompany() {
    const value = this.editForm.value;
    value.location = new Location();
    if ( value.address !== this.airCompany.location.address ) {
      value.location.address = value.address;
      this.locationService.decode(value.address).subscribe(
        (data) => {
          value.location.lat = data.results[0].geometry.location.lat;
          value.location.lon = data.results[0].geometry.location.lng;
          this.edit(value);
        },
        (error) => {
          console.log(error);
          value.location.lat = 0;
          value.location.lon = 0;
          this.edit(value);
        }
      );
    } else {
      value.location = this.airCompany.location;
      this.edit(value);
    }
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
