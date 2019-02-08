import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Room } from 'src/app/model/hotel/room';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { formatDate } from '@angular/common';
import { HotelService } from 'src/app/services/hotel/hotel.service';
import { HttpErrorResponse } from '@angular/common/http';
import { AlertService } from 'ngx-alerts';
import { RoomReservation } from 'src/app/model/hotel/room-reservation';
import { ShoppingCartService } from 'src/app/observables/shopping-cart.service';

@Component({
  selector: 'app-fast-room-display',
  templateUrl: './fast-room-display.component.html',
  styleUrls: ['./fast-room-display.component.css', '../../../shared/css/inputField.css']
})
export class FastRoomDisplayComponent implements OnInit {
  @Input() room: Room;
  @Input() hotel;
  @Input() isHotelTab;

  @Output() roomSlowed: EventEmitter<number> = new EventEmitter();

  date0 = '';
  date1 = '';
  newPrice: number;

  constructor(public tokenService: TokenStorageService,
    private hotelService: HotelService,
    private alertService: AlertService,
    private shoppingCartService: ShoppingCartService) { }


  ngOnInit() {
    this.date0 = formatDate(this.room.startDate, 'yyyy-MM-dd', 'en');
    this.date1 = formatDate(this.room.endDate, 'yyyy-MM-dd', 'en');
    this.newPrice = this.room.price - this.room.discount;
  }

  removeFromFastRooms() {
    this.hotelService.makeRoomSlow(this.hotel.id, this.room.id).subscribe(
     data => {
       this.roomSlowed.emit(this.room.id);
       this.alertService.info('Room removed from fast reservations!');
     },
     (err: HttpErrorResponse) => {
       // interceptor je hendlovao ove zahteve
       if (err.status === 401 || err.status === 403 || err.status === 404) {

       }
       // this.errorMessage = err.error.details;
     }
   );
  }

  addRoomToCart() {
    const roomReservation = new RoomReservation();
    roomReservation.checkInDate = this.room.startDate;
    roomReservation.checkOutDate = this.room.endDate;
    roomReservation.additionalServices = [];
    roomReservation.reservations = [this.room];
    if (this.hotel !== undefined) {
      roomReservation.hotel = this.hotel;
    } else {
      roomReservation.hotel = this.room.hotel;
    }

    roomReservation.isFastReservation = true;

    // set the price
    const ciDate = formatDate(roomReservation.checkInDate, 'yyyy-MM-dd hh:mm:ss', 'en');
    const coDate = formatDate(roomReservation.checkOutDate, 'yyyy-MM-dd hh:mm:ss', 'en');
    const date0 = new Date(ciDate);
    const date1 = new Date(coDate);
    const diff =  (date1.getTime() - date0.getTime()) / (1000 * 60 * 60 * 24) + 1 ;

    const totalPrice = this.room.price * diff;


    roomReservation.price = totalPrice;

    console.log('salje se u korpu ', roomReservation);
    this.shoppingCartService.changeRoomReservation(roomReservation);
  }

}
