import { Hotel } from './../../../model/hotel/hotel';
import { Component, OnInit, Input } from '@angular/core';
import { BsDatepickerConfig } from 'ngx-bootstrap';
import { Options, LabelType } from 'ng5-slider';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Room } from 'src/app/model/hotel/room';
import { formatDate } from '@angular/common';
import { AdditionalService } from 'src/app/model/additional-service';
import { HotelService } from 'src/app/services/hotel/hotel.service';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { ReservationRequest } from 'src/app/model/hotel/reservation-request';
import { HttpErrorResponse } from '@angular/common/http';
import { NgxNotificationService } from 'ngx-notification';
import { ShoppingCartService } from 'src/app/observables/shopping-cart.service';
import { RoomReservation } from 'src/app/model/hotel/room-reservation';

@Component({
  selector: 'app-book-form',
  templateUrl: './book-form.component.html',
  styleUrls: ['./book-form.component.css', '../../../shared/css/inputField.css']
})
export class BookFormComponent implements OnInit {
  @Input() rooms: Room[] = [];
  @Input() additionalServices: AdditionalService[] = [];
  visibleRooms: Room[] = []; // sobe koje ce se prikazati posle search-a
  @Input() hotel: Hotel;

  checkedAdditionalServices: AdditionalService[] = [];
  bookedRooms: Room[] = [];

  datePickerConfig: Partial<BsDatepickerConfig>;
  errorMessage: String = '';
  priceRange = [0, 200];

  bookForm: FormGroup;
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
    private hotelService: HotelService,
    private tokenService: TokenStorageService,
    public ngxNotificationService: NgxNotificationService,
    private shoppingCartService: ShoppingCartService) {

    this.datePickerConfig = Object.assign({},
      {
        containerClass: 'theme-default',
        dateInputFormat: 'YYYY-MM-DD'
      });
  }

  ngOnInit() {
    console.log('heeeej');
    this.bookForm = this.formBuilder.group({
      numberOfGuests: ['', [Validators.min(0)]],
      type: ['Regular'],
      bsRangeValue: [this.bsRangeValue],
      priceRange: [this.priceRange]
    });
  }

  onSearch() {
    this.visibleRooms = [];
    for (const room of this.rooms) {
      if (room.type === this.bookForm.value.type) {
        if (room.price >= this.bookForm.value.priceRange[0] && room.price <= this.bookForm.value.priceRange[1]) {
          if (this.isAvailable(room)) {
            this.visibleRooms.push(room);
          }
        }
      }
    }
  }

  isAvailable(room: Room) {
    if (this.bookForm.value.bsRangeValue === null) {
      this.ngxNotificationService.sendMessage('Please select a date!', 'danger', 'bottom-right' );
      return false;
    }
    const date0 = new Date(this.bookForm.value.bsRangeValue[0]);
    const datum0 = formatDate(date0, 'yyyy-MM-dd', 'en');
    const date1 = new Date(this.bookForm.value.bsRangeValue[1]);
    const datum1 = formatDate(date1, 'yyyy-MM-dd', 'en');

    for (const r of room.reservations) {
      if (datum0 >= r.roomReservation.checkInDate.toString() &&
        datum0 <= r.roomReservation.checkOutDate.toString()) {
        return false;
      }
      if (datum1 >= r.roomReservation.checkInDate.toString() &&
        datum1 <= r.roomReservation.checkOutDate.toString()) {
        return false;
      }
      if (datum0 <= r.roomReservation.checkInDate.toString() &&
        datum1 >= r.roomReservation.checkOutDate.toString()) {
        return false;
      }
    }
    return true;
  }

  clickCheck(data) {
    this.checkedAdditionalServices.push(data);
  }

  clickX(data) {
    const index: number = this.checkedAdditionalServices.indexOf(data);
    if (index !== -1) {
      this.checkedAdditionalServices.splice(index, 1);
    }
  }

  clickBook(data) {
    this.bookedRooms.push(data);
    this.ngxNotificationService.sendMessage('Room is booked.', 'dark', 'bottom-right' );
  }

  clickUnbook(data) {
    const index: number = this.bookedRooms.indexOf(data);
    if (index !== -1) {
      this.bookedRooms.splice(index, 1);
      this.ngxNotificationService.sendMessage('Room is unbooked.', 'dark', 'bottom-right' );
    }
  }

  completeBooking() {
    if (this.bookedRooms.length === 0) {
      this.ngxNotificationService.sendMessage('You must select a room!', 'danger', 'bottom-right' );
      return;
    }

    if (this.bookForm.value.bsRangeValue === null) {
      this.ngxNotificationService.sendMessage('You must select a date!', 'danger', 'bottom-right' );
      return;
    }

    const roomReservation = new RoomReservation();
    roomReservation.checkInDate = this.bookForm.value.bsRangeValue[0];
    roomReservation.checkOutDate = this.bookForm.value.bsRangeValue[1];
    console.log('this.bookForm.value.bsRangeValue[1]', this.bookForm.value.bsRangeValue[1]);
    roomReservation.additionalServices = this.checkedAdditionalServices;
    roomReservation.reservations = this.bookedRooms;
    roomReservation.hotel = this.hotel;
    roomReservation.isFastReservation = false;

    // set the price
    const ciDate = formatDate(roomReservation.checkInDate, 'yyyy-MM-dd hh:mm:ss', 'en');
    const coDate = formatDate(roomReservation.checkOutDate, 'yyyy-MM-dd hh:mm:ss', 'en');
    const date0 = new Date(ciDate);
    const date1 = new Date(coDate);
    const diff =  (date1.getTime() - date0.getTime()) / (1000 * 60 * 60 * 24) + 1 ;

    let totalPrice = 0;

    for (const res of roomReservation.reservations) {
      totalPrice += diff * res.price;
    }
    for (const as of roomReservation.additionalServices) {
      totalPrice += as.price;
    }

    roomReservation.price = totalPrice;

    console.log('salje se u korpu ', roomReservation);
    this.shoppingCartService.changeRoomReservation(roomReservation); // Ubaci u korpu
    /*this.hotelService.rentRoom(roomReservation, this.hotelId, this.tokenService.getUsername()).subscribe(
      roomReservationData => {
        console.log('vraceno', roomReservationData);
        this.ngxNotificationService.sendMessage('Booking is completed!', 'dark', 'bottom-right' );
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
    this.visibleRooms = [];
    this.bookedRooms = [];
  }
}
