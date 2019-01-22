import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Car } from 'src/app/model/rent-a-car-company/car';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs/Subject';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-new-car-form',
  templateUrl: './new-car-form.component.html',
  styleUrls: ['./new-car-form.component.css', '../../../shared/css/inputField.css']
})

export class NewCarFormComponent implements OnInit {
  errorMessage: String = '';

  car: Car = new Car();
  @Input() carCompanyId: Number;

  public onClose: Subject<Car>;
  newCarForm: FormGroup;

  constructor(public modalRef: BsModalRef, private formBuilder: FormBuilder, private route: ActivatedRoute,
     private rentACarCompanyService: RentACarCompanyService) { }

  ngOnInit() {
    this.car.type = 'Sedan';
    this.onClose = new Subject();
    this.newCarForm = this.formBuilder.group({
      brand: [this.car.brand, [Validators.required]],
      model: [this.car.model, [Validators.required]],
      type: [this.car.type],
      yearOfProduction: [this.car.yearOfProduction, [Validators.required, Validators.min(0)]],
      seatsNumber: [this.car.seatsNumber, [Validators.required, Validators.min(1)]],
      doorsNumber: [this.car.doorsNumber, [Validators.required, Validators.min(1)]],
      price: [this.car.price, [Validators.required, Validators.min(0)]],
    });
  }

  onCarAdd() {
    console.log(this.car);

    this.rentACarCompanyService.addCar(this.newCarForm.value, this.carCompanyId).subscribe(
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
