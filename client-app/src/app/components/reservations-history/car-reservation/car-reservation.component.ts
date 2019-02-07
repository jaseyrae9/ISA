import { Component, OnInit, Input } from '@angular/core';
import { CarReservation } from 'src/app/model/rent-a-car-company/car-reservation';
import { formatDate, DatePipe } from '@angular/common';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { HttpErrorResponse } from '@angular/common/http';
import { NgxNotificationService } from 'ngx-notification';

@Component({
  selector: 'app-car-reservation',
  templateUrl: './car-reservation.component.html',
  styleUrls: ['./car-reservation.component.css', '../../../shared/css/inputField.css']
})
export class CarReservationComponent implements OnInit {
  @Input() reservation: CarReservation;
  @Input() useCancel = false;

  max = 5;

  diff: number;
  totalPrice: number;
  pickUp = '';
  dropOff = '';
  date0 = new Date();
  date1 = new Date();
  constructor(private carService: RentACarCompanyService,
    private ngxNotificationService: NgxNotificationService,
    public datePipe: DatePipe) { }

  ngOnInit() {
    this.pickUp = formatDate(this.reservation.pickUpDate, 'yyyy-MM-dd hh:mm:ss', 'en');
    this.dropOff = formatDate(this.reservation.dropOffDate, 'yyyy-MM-dd hh:mm:ss', 'en');
    this.date0 = new Date(this.pickUp);
    this.date1 = new Date(this.dropOff);
    this.diff =  (this.date1.getTime() - this.date0.getTime()) / (1000 * 60 * 60 * 24) + 1 ;
   // console.log('razlikaaa', this.diff);
    this.totalPrice = this.diff * this.reservation.car.price;
  }

  rateCarCompany() {
    if (this.reservation.isCompanyRated !== true) {
      console.log('Ocenjivanje kompanije', this.reservation.car.rentACarCompany.averageRating);

      this.carService.rateCarCompany(this.reservation.car.rentACarCompany.id,
         this.reservation.id, this.reservation.car.rentACarCompany.averageRating).subscribe(
        newRate => {
          console.log('vraceno', newRate);
          this.reservation.isCompanyRated = true;
          this.ngxNotificationService.sendMessage('You have rated a car company!', 'dark', 'bottom-right' );
        },
        (err: HttpErrorResponse) => {
          // interceptor je hendlovao ove zahteve
          if (err.status === 401 || err.status === 403 || err.status === 404) {
            // refresh
          }
          this.ngxNotificationService.sendMessage(err.error.details, 'danger', 'bottom-right' );
          // this.errorMessage = err.error.details;
        }
      );
    }
  }

  rateCar() {
    if (this.reservation.isCarRated !== true) {
      console.log('Ocenjivanje automobila', this.reservation.car.averageRating);

      this.carService.rateCar(this.reservation.car.id,
        this.reservation.id, this.reservation.car.averageRating).subscribe(
       newRate => {
         console.log('vraceno', newRate);
         this.reservation.isCarRated = true;
         this.ngxNotificationService.sendMessage('You have rated a car!', 'dark', 'bottom-right' );
       },
       (err: HttpErrorResponse) => {
         // interceptor je hendlovao ove zahteve
         if (err.status === 401 || err.status === 403 || err.status === 404) {
           // refresh
         }
         this.ngxNotificationService.sendMessage(err.error.details, 'danger', 'bottom-right' );
         // this.errorMessage = err.error.details;
       }
     );
    }
  }


  cancelCarReservation() {
    this.carService.cancelCarReservation(this.reservation.id).subscribe(
     (data) => {
       console.log('Otkazana rezervacija id: ', data);
       this.reservation.active = false;
       this.ngxNotificationService.sendMessage('You have canceled car reservation!', 'dark', 'bottom-right' );
     },
     (err: HttpErrorResponse) => {
       // interceptor je hendlovao ove zahteve
       if (err.status === 401 || err.status === 403 || err.status === 404) {
         // refresh
       }
       this.ngxNotificationService.sendMessage(err.error.details, 'danger', 'bottom-right' );
       // this.errorMessage = err.error.details;
     }
   );
  }
}
