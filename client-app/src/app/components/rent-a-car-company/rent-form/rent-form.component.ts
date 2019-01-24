import { Component, OnInit, Input } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { BranchOffice } from 'src/app/model/rent-a-car-company/branch-offfice';
import { Car } from 'src/app/model/rent-a-car-company/car';
import { BsDatepickerConfig } from 'ngx-bootstrap';
import { formatDate } from '@angular/common';

@Component({
  selector: 'app-rent-form',
  templateUrl: './rent-form.component.html',
  styleUrls: ['./rent-form.component.css', '../../../shared/css/inputField.css']
})

export class RentFormComponent implements OnInit {
  datePickerConfig: Partial<BsDatepickerConfig>;

  errorMessage: String = '';
  @Input() branchOffices: BranchOffice[];
  @Input() cars: Car[];

  visibleCars: Car[] = []; // koja ce se prikazati posle search-a

  rentForm: FormGroup;
  bsRangeValue: Date[];

  constructor(private formBuilder: FormBuilder) {
    this.datePickerConfig = Object.assign({},
      {
        containerClass: 'theme-default',
        dateInputFormat: 'YYYY-MM-DD'
      });
  }

  ngOnInit() {
    this.rentForm = this.formBuilder.group({
      numberOfPassengers: ['', [Validators.min(0)]],
      type: ['Sedan'],
      bsRangeValue: [this.bsRangeValue]
    });
  }

  onSearch() {
    this.visibleCars = [];
    console.log('izabrani tip ' + this.rentForm.value.type);
    for (const car of this.cars) {
      console.log('naisli na tip ' + car.type);
      if (car.type === this.rentForm.value.type &&
        car.seatsNumber >= this.rentForm.value.numberOfPassengers) {
        console.log('model ' + car.model + ' tip ' + car.type);
        if (this.isAvailable(car)) {
          console.log('dostupno');
          this.visibleCars.push(car);
        }

      }
    }
  }

  isAvailable(car: Car) {
    console.log('Automobil' + car.model);

    console.log('this.rentForm.value.bsRangeValue[0]: ' + this.rentForm.value.bsRangeValue[0].getDate());
    console.log('this.rentForm.value.bsRangeValue[1]: ' + this.rentForm.value.bsRangeValue[1]);
    const date0 = new Date(this.rentForm.value.bsRangeValue[0]);
    const datum0 = formatDate(date0, 'yyyy-MM-dd', 'en');
    const date1 = new Date(this.rentForm.value.bsRangeValue[1]);
    const datum1 = formatDate(date1, 'yyyy-MM-dd', 'en');

    console.log('datum0 ' + datum0);
    console.log('datum1' + datum1);

    for (const r of car.carReservations) {
       console.log('r.pickUpDate: ' + r.pickUpDate.toString());
       console.log('r.dropOffDate: ' + r.dropOffDate.toString());

      if (datum0 >= r.pickUpDate.toString() &&
      datum0 <= r.dropOffDate.toString()) {
        return false;
      }
      if (datum1 >= r.pickUpDate.toString() &&
      datum1 <= r.dropOffDate.toString()) {
        return false;
      }
    }
    return true;
  }
}
