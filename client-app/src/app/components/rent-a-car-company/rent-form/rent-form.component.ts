import { Component, OnInit, Input, ViewChild, ElementRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { BranchOffice } from 'src/app/model/rent-a-car-company/branch-offfice';
import { Car } from 'src/app/model/rent-a-car-company/car';
import { BsDatepickerConfig } from 'ngx-bootstrap';
import { formatDate } from '@angular/common';
import { Options, LabelType } from 'ng5-slider';
import { CarReservation } from 'src/app/model/rent-a-car-company/car-reservation';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';

@Component({
  selector: 'app-rent-form',
  templateUrl: './rent-form.component.html',
  styleUrls: ['./rent-form.component.css', '../../../shared/css/inputField.css']
})

export class RentFormComponent implements OnInit {
  datePickerConfig: Partial<BsDatepickerConfig>;

  errorMessage: String = '';
  @Input() branchOffices: BranchOffice[];
  @Input() cars: Car[] = [];

  visibleCars: Car[] = []; // koja ce se prikazati posle search-a

  priceRange = [0, 200];

  rentForm: FormGroup;
  bsRangeValue: Date[];

  minValue = 0;
  maxValue = 200;

  options: Options = {
    floor: 0,
    ceil: 200,
    showSelectionBar: true,
    selectionBarGradient: {
      from: 'white',
      to: '#33cabb'
    },

    getPointerColor: (value: number): string => {
      return '#33cabb';
    },

    translate: (value: number, label: LabelType): string => {
      switch (label) {
        case LabelType.Low:
          return '<b>Min price:</b> $' + value;
        case LabelType.High:
          return '<b>Max price:</b> $' + value;
        default:
          return '$' + value;
      }
    }
  };

  constructor(private formBuilder: FormBuilder,
    public tokenStorageService: TokenStorageService,
    private companyService: RentACarCompanyService) {
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
      bsRangeValue: [this.bsRangeValue],
      priceRange: [this.priceRange],
      dropOffBranchOffice: [],
      pickUpBranchOffice: []
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
        if (car.price >= this.rentForm.value.priceRange[0] && car.price <= this.rentForm.value.priceRange[1]) {
          if (this.isAvailable(car)) {
            console.log('dostupno');
            this.visibleCars.push(car);
          }
        }
      }
    }
  }

  isAvailable(car: Car) {
    console.log(this.rentForm.value.priceRange);

    console.log('Automobil' + car.model);
    const date0 = new Date(this.rentForm.value.bsRangeValue[0]);
    const datum0 = formatDate(date0, 'yyyy-MM-dd', 'en');
    const date1 = new Date(this.rentForm.value.bsRangeValue[1]);
    const datum1 = formatDate(date1, 'yyyy-MM-dd', 'en');

    for (const r of car.carReservations) {

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

  carRented(data) {
    console.log('carRented(data)', data);
    const cr: CarReservation = new CarReservation();
    cr.pickUpBranchOffice.id = this.rentForm.value.pickUpBranchOffice;
    cr.dropOffBranchOffice.id = this.rentForm.value.dropOffBranchOffice;
    const date0 = new Date(this.rentForm.value.bsRangeValue[0]);
    const date1 = new Date(this.rentForm.value.bsRangeValue[1]);
    cr.pickUpDate = date0;
    cr.dropOffDate =  date1;
    this.companyService.rentCar(cr, data.id, this.tokenStorageService.getUsername()).subscribe(
      carReservation => {
        console.log('vraceno', carReservation);
      },
      error => {
        console.log(error);
      }
    );
  }

  onChange() {
    this.visibleCars = [];
  }
}
