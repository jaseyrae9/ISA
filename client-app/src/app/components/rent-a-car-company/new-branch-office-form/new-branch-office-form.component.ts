import { Component, OnInit, Output, ViewChild, ElementRef, EventEmitter, Input } from '@angular/core';
import { BranchOffice } from 'src/app/model/rent-a-car-company/branch-offfice';
import { ActivatedRoute } from '@angular/router';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs/Subject';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { LocationService } from 'src/app/services/location-service';

@Component({
  selector: 'app-new-branch-office-form',
  templateUrl: './new-branch-office-form.component.html',
  styleUrls: ['../../../shared/css/inputField.css', './new-branch-office-form.component.css']
})
export class NewBranchOfficeFormComponent implements OnInit {
  errorMessage: String = '';

  public onClose: Subject<BranchOffice>;
  branchOffice: BranchOffice = new BranchOffice();
  @Input() carCompanyId: Number;
  newBranchOfficeForm: FormGroup;

  constructor(public modalRef: BsModalRef,
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private rentACarCompanyService: RentACarCompanyService,
    private locationService: LocationService) { }

  ngOnInit() {
    this.onClose = new Subject();
    this.newBranchOfficeForm = this.formBuilder.group({
      name: [this.branchOffice.name, [Validators.required]],
      city: [this.branchOffice.location.city, [Validators.required]],
      country: [this.branchOffice.location.country, [Validators.required]],
      address: [this.branchOffice.location.address, [Validators.required]]
    });
  }


  onBranchOfficeAdd() {
    this.locationService.decode(this.newBranchOfficeForm.value.address).subscribe(
      (data) => {
        console.log('data', data);
        this.branchOffice.location.lat = data.results[0].geometry.location.lat;
        this.branchOffice.location.lon = data.results[0].geometry.location.lng;
        this.add();
      },
      (error) => {
        this.branchOffice.location.lat = 0;
        this.branchOffice.location.lon = 0;
        this.add();
      }
    );
  }

  add() {
    this.branchOffice.name = this.newBranchOfficeForm.value.name;
    this.branchOffice.location.address = this.newBranchOfficeForm.value.address;
    this.branchOffice.location.city = this.newBranchOfficeForm.value.city;
    this.branchOffice.location.country = this.newBranchOfficeForm.value.country;
    console.log('Salje se filijala: ', this.branchOffice);
    this.rentACarCompanyService.addBranchOffice(this.branchOffice, this.carCompanyId).subscribe(
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

}
