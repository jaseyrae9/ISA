import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Car } from 'src/app/model/rent-a-car-company/car';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { formatDate } from '@angular/common';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { HttpErrorResponse } from '@angular/common/http';
import { CarReservation } from 'src/app/model/rent-a-car-company/car-reservation';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';
import { ShoppingCartService } from 'src/app/observables/shopping-cart.service';

@Component({
  selector: 'app-fast-reservation-display',
  templateUrl: './fast-reservation-display.component.html',
  styleUrls: ['./fast-reservation-display.component.css', '../../../shared/css/inputField.css']
})
export class FastReservationDisplayComponent implements OnInit {
  @Input() car: Car;
  @Input() carCompany: RentACarCompany;
  @Output() carRemovedFromFastReservations: EventEmitter<number> = new EventEmitter();
  errorMessage = '';
  @Input() isCarPage = true;

  constructor(public tokenService: TokenStorageService,
    private rentACarCompanyService: RentACarCompanyService,
    private shoppingCartService: ShoppingCartService) { }

  date0 = '';
  date1 = '';
  newPrice: number;

  ngOnInit() {
    this.date0 = formatDate(this.car.beginDate, 'yyyy-MM-dd', 'en');
    this.date1 = formatDate(this.car.endDate, 'yyyy-MM-dd', 'en');
    this.newPrice = this.car.price - this.car.discount;
  }

  removeFromFastCar() {
    this.rentACarCompanyService.removeCarFromFastReservations(this.carCompany.id, this.car.id).subscribe(
      data => {
        this.carRemovedFromFastReservations.emit(this.car.id);
      },
      (err: HttpErrorResponse) => {
        // interceptor je hendlovao ove zahteve
        if (err.status === 401 || err.status === 403 || err.status === 404) {
        }
        this.errorMessage = err.error.details;
      }
    );
  }

  addToCart() {
    const cr: CarReservation = new CarReservation();
    cr.pickUpBranchOffice.id = this.car.fastPickUpBranchOffice.id;
    cr.dropOffBranchOffice.id = this.car.fastDropOffBranchOffice.id;
    cr.pickUpDate = this.car.beginDate;
    cr.dropOffDate =  this.car.endDate;
    cr.rentACarCompany = this.carCompany;
    cr.car = this.car;
    cr.isFastReservation = true;

    // set the price
    const ciDate = formatDate(this.car.beginDate, 'yyyy-MM-dd hh:mm:ss', 'en');
    const coDate = formatDate(this.car.endDate, 'yyyy-MM-dd hh:mm:ss', 'en');
    const dateCi = new Date(ciDate);
    const dateCo = new Date(coDate);
    const diff =  (dateCo.getTime() - dateCi.getTime()) / (1000 * 60 * 60 * 24) + 1 ;

    const totalPrice = diff * cr.car.price;

    cr.price = totalPrice;

    this.shoppingCartService.changeCarReservation(cr);
  }

}
