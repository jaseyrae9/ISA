import { Component, OnInit, Input } from '@angular/core';
import { RoomReservation } from 'src/app/model/hotel/room-reservation';
import { formatDate, DatePipe } from '@angular/common';
import { HotelService } from 'src/app/services/hotel/hotel.service';
import { AlertService } from 'ngx-alerts';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-hotel-reservation',
  templateUrl: './hotel-reservation.component.html',
  styleUrls: ['./hotel-reservation.component.css', './../../../shared/css/inputField.css']
})
export class HotelReservationComponent implements OnInit {
  @Input() reservation: RoomReservation;
  @Input() useCancel = false;


  max = 5;
  rateCompany = 3;
  hotelRate = 0;

  diff: number;
  totalPrice = 0;
  checkIn = '';
  checkOut = '';
  date0 = new Date();
  date1 = new Date();

  constructor(private hotelService: HotelService,
    private alertService: AlertService,
    public datePipe: DatePipe) { }

  ngOnInit() {
    this.checkIn = formatDate(this.reservation.checkInDate, 'yyyy-MM-dd hh:mm:ss', 'en');
    this.checkOut = formatDate(this.reservation.checkOutDate, 'yyyy-MM-dd hh:mm:ss', 'en');
    this.date0 = new Date(this.checkIn);
    this.date1 = new Date(this.checkOut);
    this.diff =  (this.date1.getTime() - this.date0.getTime()) / (1000 * 60 * 60 * 24) + 1 ;
    console.log('AArazlikaaa', this.diff);
    // this.totalPrice = this.diff * this.reservation.reservations.;
    for (const reservation of this.reservation.roomReservations) {
      console.log('Cena', reservation.price);
      this.totalPrice += this.diff * reservation.price;

      reservation.rating = reservation.totalRating / reservation.ratingCount;

    }
    for (const as of this.reservation.additionalServices) {
      this.totalPrice += as.price;
    }

    this.hotelRate = this.reservation.hotel.averageRating;
  }

  rateRoom(roomId) {
    let rating = 0;
    for (const res of this.reservation.roomReservations) {
      if ((res.id === roomId)) {
        if (res.singleRoomReservations[0].isRoomRated !== true) {
          rating = res.rating;

          console.log('Ocenjivanje sobe ' + roomId + 'Ocena: ' + rating);

          this.hotelService.rateRoom(roomId, res.singleRoomReservations[0].id, rating).subscribe(
            newRate => {
              console.log('Nova ocena sobe', newRate);
              res.singleRoomReservations[0].isRoomRated = true;
              this.alertService.info('You have rated a room!');
            },
            (err: HttpErrorResponse) => {
              // interceptor je hendlovao ove zahteve
              if (err.status === 401 || err.status === 403 || err.status === 404) {
                // refresh
              }
              this.alertService.info(err.error.details);
              // this.errorMessage = err.error.details;
            }
          );
          break;
        }
      }
    }
  }


  rateHotel() {
    if (this.reservation.isHotelRated !== true) {
      console.log('Ocenjivanje hotela');
      this.hotelService.rateHotel(this.reservation.hotel.id, this.reservation.id, this.hotelRate).subscribe(
        newRate => {
          console.log('vraceno', newRate);
          this.reservation.isHotelRated = true;
          this.alertService.info('You have rated a hotel!');
        },
        (err: HttpErrorResponse) => {
          // interceptor je hendlovao ove zahteve
          if (err.status === 401 || err.status === 403 || err.status === 404) {
            // refresh
          }
          this.alertService.info(err.error.details);
          // this.errorMessage = err.error.details;
        }
      );
    }
  }

  cancelRoomReservation() {
    this.hotelService.cancelRoomReservation(this.reservation.id).subscribe(
      data => {
        this.reservation.active = false;
        this.alertService.info('You have canceled car reservation!');
      },
      (err: HttpErrorResponse) => {
        // interceptor je hendlovao ove zahteve
        if (err.status === 401 || err.status === 403 || err.status === 404) {
          // refresh
        }
        this.alertService.info(err.error.details);
        // this.errorMessage = err.error.details;
      }
    );
  }
}
