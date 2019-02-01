import { Component, OnInit, Input } from '@angular/core';
import { BranchOffice } from 'src/app/model/rent-a-car-company/branch-offfice';
import { Subject } from 'rxjs/Subject';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { LocationService } from 'src/app/services/location-service';
import { Location } from 'src/app/model/location';

@Component({
  selector: 'app-edit-branch-office-form',
  templateUrl: './edit-branch-office-form.component.html',
  styleUrls: ['./edit-branch-office-form.component.css', '../../../shared/css/inputField.css']
})
export class EditBranchOfficeFormComponent implements OnInit {
  errorMessage: String = '';
  @Input() branchOffice: BranchOffice;
  @Input() companyId: string;
  public onClose: Subject<BranchOffice>;
  editBranchOfficeForm: FormGroup;

  constructor(private formBuilder: FormBuilder,
    private carCompanyService: RentACarCompanyService,
    public modalRef: BsModalRef,
    private locationService: LocationService) { }

  ngOnInit() {
    this.onClose = new Subject();
    this.editBranchOfficeForm = this.formBuilder.group({
      id: [this.branchOffice.id],
      name: [this.branchOffice.name, [Validators.required]],
      city: [this.branchOffice.location.city, [Validators.required]],
      country: [this.branchOffice.location.country, [Validators.required]],
      address: [this.branchOffice.location.address, [Validators.required]],
    });
  }

  onEditBranchOffice() {
    const value = this.editBranchOfficeForm.value;
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
      value.location = this.branchOffice.location;
      this.submit(value);
    }
  }


  submit(value) {
    this.carCompanyService.editBranchOffice(value, this.companyId).subscribe(
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
    return value.address !== this.branchOffice.location.address || value.city !== this.branchOffice.location.city
      || value.country !== this.branchOffice.location.country;
  }
}
