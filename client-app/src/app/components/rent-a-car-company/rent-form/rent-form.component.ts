import { Component, OnInit, Input, ViewChild, ElementRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BranchOffice } from 'src/app/model/rent-a-car-company/branch-offfice';
import { Car } from 'src/app/model/rent-a-car-company/car';
import { BsDatepickerConfig } from 'ngx-bootstrap';
import { formatDate } from '@angular/common';
import { Options, LabelType } from 'ng5-slider';
import { CarReservation } from 'src/app/model/rent-a-car-company/car-reservation';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { HttpErrorResponse } from '@angular/common/http';
import { NgxNotificationService } from 'ngx-notification';
import { ShoppingCartService } from 'src/app/observables/shopping-cart.service';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';

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
  @Input() carCompany: RentACarCompany;

  visibleCars: Car[] = []; // kola koja ce se prikazati posle search-a

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
    private companyService: RentACarCompanyService,
    public ngxNotificationService: NgxNotificationService,
    private shoppingCartService: ShoppingCartService) {
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
      dropOffBranchOffice: [''],
      pickUpBranchOffice: ['']
    });
  }

  onSearch() {
    this.visibleCars = [];
    for (const car of this.cars) {
      if (car.type === this.rentForm.value.type &&
        car.seatsNumber >= this.rentForm.value.numberOfPassengers) {
        if (car.price >= this.rentForm.value.priceRange[0] && car.price <= this.rentForm.value.priceRange[1]) {
          if (this.isAvailable(car)) {
            this.visibleCars.push(car);
          }
        }
      }
    }
  }

  isAvailable(car: Car) {
    const date0 = new Date(this.rentForm.value.bsRangeValue[0]);
    const datum0 = formatDate(date0, 'yyyy-MM-dd', 'en');
    const date1 = new Date(this.rentForm.value.bsRangeValue[1]);
    const datum1 = formatDate(date1, 'yyyy-MM-dd', 'en');

    for (const r of car.reservations) {
      if (datum0 >= r.pickUpDate.toString() &&
        datum0 <= r.dropOffDate.toString()) {
        return false;
      }
      if (datum1 >= r.pickUpDate.toString() &&
        datum1 <= r.dropOffDate.toString()) {
        return false;
      }
      if (datum0 <= r.pickUpDate.toString() &&
        datum1 >= r.dropOffDate.toString()) {
        return false;
      }
    }
    return true;
  }

  carRented(data) {
    console.log('form value ', this.rentForm.value);
    console.log('Rentano auto: ', data);

    const cr: CarReservation = new CarReservation();
    cr.pickUpBranchOffice.id = this.rentForm.value.pickUpBranchOffice;
    cr.dropOffBranchOffice.id = this.rentForm.value.dropOffBranchOffice;
    const date0 = new Date(this.rentForm.value.bsRangeValue[0]);
    const date1 = new Date(this.rentForm.value.bsRangeValue[1]);
    cr.pickUpDate = date0;
    cr.dropOffDate =  date1;
    cr.rentACarCompany = this.carCompany;
    cr.car = data;
    cr.isFastReservation = false;

    // set the price
    const ciDate = formatDate(date0, 'yyyy-MM-dd hh:mm:ss', 'en');
    const coDate = formatDate(date1, 'yyyy-MM-dd hh:mm:ss', 'en');
    const dateCi = new Date(ciDate);
    const dateCo = new Date(coDate);
    const diff =  (dateCo.getTime() - dateCi.getTime()) / (1000 * 60 * 60 * 24) + 1 ;

    const totalPrice = diff * cr.car.price;

    cr.price = totalPrice;
    cr.rentACarCompany = this.carCompany;

    this.shoppingCartService.changeCarReservation(cr);

    /*this.companyService.rentCar(cr, this.carCompanyId, data.id, this.tokenStorageService.getUsername()).subscribe(
      carReservation => {

        console.log('vraceno', carReservation);
      },
      (err: HttpErrorResponse) => {
        // interceptor je hendlovao ove zahteve
        if (err.status === 401 || err.status === 403 || err.status === 404) {
          // refresh
        }
        this.errorMessage = err.error.details;
      }
    );*/
  }

  onChange() {
    this.visibleCars = [];
  }
}
