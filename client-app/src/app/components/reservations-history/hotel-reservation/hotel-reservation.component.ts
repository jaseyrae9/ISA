import { Component, OnInit, Input } from '@angular/core';
import { RoomReservation } from 'src/app/model/hotel/room-reservation';
import { formatDate } from '@angular/common';
import { HotelService } from 'src/app/services/hotel/hotel.service';
import { NgxNotificationService } from 'ngx-notification';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-hotel-reservation',
  templateUrl: './hotel-reservation.component.html',
  styleUrls: ['./hotel-reservation.component.css', './../../../shared/css/inputField.css']
})
export class HotelReservationComponent implements OnInit {
  @Input() reservation: RoomReservation;


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
    private ngxNotificationService: NgxNotificationService) { }

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
              this.ngxNotificationService.sendMessage('You have rated a room!', 'dark', 'bottom-right' );
            },
            (err: HttpErrorResponse) => {
              // interceptor je hendlovao ove zahteve
              if (err.status === 401 || err.status === 403 || err.status === 404) {
                // refresh
              }
              this.ngxNotificationService.sendMessage('Error!', 'danger', 'bottom-right' );
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
          this.ngxNotificationService.sendMessage('You have rated a hotel!', 'dark', 'bottom-right' );
        },
        (err: HttpErrorResponse) => {
          // interceptor je hendlovao ove zahteve
          if (err.status === 401 || err.status === 403 || err.status === 404) {
            // refresh
          }
          this.ngxNotificationService.sendMessage('Error!', 'danger', 'bottom-right' );
          // this.errorMessage = err.error.details;
        }
      );
    }
  }
}
