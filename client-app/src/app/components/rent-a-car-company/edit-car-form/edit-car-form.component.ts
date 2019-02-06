import { Component, OnInit, Input, ViewChild, ElementRef, Output, EventEmitter } from '@angular/core';
import { Car } from 'src/app/model/rent-a-car-company/car';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs/Subject';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-edit-car-form',
  templateUrl: './edit-car-form.component.html',
  styleUrls: ['./edit-car-form.component.css', '../../../shared/css/inputField.css']
})
export class EditCarFormComponent implements OnInit {
  errorMessage: String = '';
  @Input() car: Car;
  @Input() companyId: string;
  public onClose: Subject<Car>;
  editCarForm: FormGroup;

  constructor(private formBuilder: FormBuilder,
    private carCompanyService: RentACarCompanyService,
    public modalRef: BsModalRef) { }

  ngOnInit() {
    this.onClose = new Subject();
    this.editCarForm = this.formBuilder.group({
    id: [this.car.id],
    brand: [this.car.brand, [Validators.required]],
    model: [this.car.model, [Validators.required]],
    type: [this.car.type],
    yearOfProduction: [this.car.yearOfProduction, [Validators.required, Validators.min(0)]],
    seatsNumber: [this.car.seatsNumber, [Validators.required, Validators.min(1)]],
    doorsNumber: [this.car.doorsNumber, [Validators.required, Validators.min(1)]],
    price: [this.car.price, [Validators.required, Validators.min(0), Validators.max(500)]],
    });
  }

  onEditCar() {
    this.carCompanyService.editCar(this.editCarForm.value, this.companyId).subscribe(
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
