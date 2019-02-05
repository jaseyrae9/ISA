import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Room } from 'src/app/model/hotel/room';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { formatDate } from '@angular/common';
import { HotelService } from 'src/app/services/hotel/hotel.service';
import { HttpErrorResponse } from '@angular/common/http';
import { NgxNotificationService } from 'ngx-notification';

@Component({
  selector: 'app-fast-room-display',
  templateUrl: './fast-room-display.component.html',
  styleUrls: ['./fast-room-display.component.css', '../../../shared/css/inputField.css']
})
export class FastRoomDisplayComponent implements OnInit {
  @Input() room: Room;
  @Input() hotelId;
  @Input() isHotelTab;

  @Output() roomSlowed: EventEmitter<number> = new EventEmitter();

  date0 = '';
  date1 = '';
  newPrice: number;

  constructor(public tokenService: TokenStorageService,
    private hotelService: HotelService,
    private ngxNotificationService: NgxNotificationService) { }


  ngOnInit() {
    this.date0 = formatDate(this.room.startDate, 'yyyy-MM-dd', 'en');
    this.date1 = formatDate(this.room.endDate, 'yyyy-MM-dd', 'en');
    this.newPrice = this.room.price - this.room.discount;
  }

  removeFromFastRooms() {
    this.hotelService.makeRoomSlow(this.hotelId, this.room.id).subscribe(
     data => {
       this.roomSlowed.emit(this.room.id);
       this.ngxNotificationService.sendMessage('Room removed from fast reservations!', 'dark', 'bottom-right');
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
    
  }

}
