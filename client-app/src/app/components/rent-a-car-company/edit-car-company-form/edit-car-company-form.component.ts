import { Component, OnInit, Input } from '@angular/core';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';
import { RentACarCompanyService} from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs/Subject';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { LocationService } from 'src/app/services/location-service';
import { Location } from 'src/app/model/location';

@Component({
  selector: 'app-edit-car-company-form',
  templateUrl: './edit-car-company-form.component.html',
  styleUrls: ['./edit-car-company-form.component.css',  '../../../shared/css/inputField.css']
})
export class EditCarCompanyFormComponent implements OnInit {
  errorMessage: String = '';
  public onClose: Subject<RentACarCompany>;
  @Input() carCompany: RentACarCompany;
  editCarCompanyForm: FormGroup;

  constructor (private formBuilder: FormBuilder,
    public modalRef: BsModalRef,
    private carCompanyService: RentACarCompanyService,
    private locationService: LocationService) { }

  ngOnInit() {
    this.onClose = new Subject();
    this.editCarCompanyForm = this.formBuilder.group({
      id: [this.carCompany.id],
      name: [this.carCompany.name, [Validators.required]],
      city: [this.carCompany.location.city, [Validators.required]],
      country: [this.carCompany.location.country, [Validators.required]],
      address: [this.carCompany.location.address, [Validators.required]],
      description: [this.carCompany.description],
      });
  }

  onSubmit() {
    const value = this.editCarCompanyForm.value;
    value.location = new Location();
    if (this.addressChanged(value)) {
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
      value.location = this.carCompany.location;
      this.submit(value);
    }
  }

  submit(value) {
    this.carCompanyService.edit(value).subscribe(
      data => {
        this.onClose.next(data);
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

  addressChanged(value): boolean {
    return value.address !== this.carCompany.location.address || value.city !== this.carCompany.location.city
      || value.country !== this.carCompany.location.country;
  }
}
