import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Car } from 'src/app/model/rent-a-car-company/car';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { formatDate } from '@angular/common';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-fast-reservation-display',
  templateUrl: './fast-reservation-display.component.html',
  styleUrls: ['./fast-reservation-display.component.css', '../../../shared/css/inputField.css']
})
export class FastReservationDisplayComponent implements OnInit {
  @Input() car: Car;
  @Input() carCompanyId: number;
  @Output() carRemovedFromFastReservations: EventEmitter<number> = new EventEmitter();
  errorMessage = '';
  @Input() isCarPage = true;

  constructor(public tokenService: TokenStorageService,
    private rentACarCompanyService: RentACarCompanyService) { }

  date0 = '';
  date1 = '';
  newPrice: number;

  ngOnInit() {
    this.date0 = formatDate(this.car.beginDate, 'yyyy-MM-dd', 'en');
    this.date1 = formatDate(this.car.endDate, 'yyyy-MM-dd', 'en');
    this.newPrice = this.car.price - this.car.discount;
  }

  removeFromFastCar() {
    this.rentACarCompanyService.removeCarFromFastReservations(this.carCompanyId, this.car.id).subscribe(
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

}
