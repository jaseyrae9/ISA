import { Component, OnInit, Output, EventEmitter, ViewChild, ElementRef } from '@angular/core';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';
import { RentACarCompanyService} from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs/Subject';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { LocationService } from './../../../services/location-service';

@Component({
  selector: 'app-new-car-company-form',
  templateUrl: './new-car-company-form.component.html',
  styleUrls: ['../../../shared/css/inputField.css']
})
export class NewCarCompanyFormComponent implements OnInit {
  errorMessage: String = '';

  carCompany: RentACarCompany = new RentACarCompany();
  public onClose: Subject<RentACarCompany>;
  newCarCompanyForm: FormGroup;

  constructor(public modalRef: BsModalRef,
    private formBuilder: FormBuilder,
    private rentACarCompanyService: RentACarCompanyService,
    private locationService: LocationService) { }

  ngOnInit() {
    this.onClose = new Subject();
    this.newCarCompanyForm = this.formBuilder.group({
      name: [this.carCompany.name, [Validators.required]],
      address: [this.carCompany.location.address,  [Validators.required]],
      description: [this.carCompany.description],
      });
  }

  onCarCompanyAdd() {
    this.locationService.decode(this.newCarCompanyForm.value.address).subscribe(
      (data) => {
        this.carCompany.location.lat = data.results[0].geometry.location.lat;
        this.carCompany.location.lon = data.results[0].geometry.location.lng;
        this.add();
      },
      (error) => {
        this.carCompany.location.lat = 0;
        this.carCompany.location.lon = 0;
        this.add();
      }
    );
  }

  add() {
    this.carCompany.name = this.newCarCompanyForm.value.name;
    this.carCompany.description = this.newCarCompanyForm.value.description;
    this.carCompany.location.address = this.newCarCompanyForm.value.address;

    this.rentACarCompanyService.add(this.carCompany).subscribe(
      data => {
        this.onClose.next(data);
        this.modalRef.hide();
      },
      (err: HttpErrorResponse) => {
        if (err.status === 401 || err.status === 403 || err.status === 404) {
          this.modalRef.hide();
        }
        this.errorMessage = err.error.details;
      }
    );
  }

}
