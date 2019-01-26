import { Component, OnInit, Input } from '@angular/core';
import { BsDatepickerConfig } from 'ngx-bootstrap';
import { Options, LabelType } from 'ng5-slider';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Room } from 'src/app/model/hotel/room';
import { formatDate } from '@angular/common';
import { AdditionalService } from 'src/app/model/additional-service';
import { RoomReservation } from 'src/app/model/hotel/room-reservation';
import { HotelService } from 'src/app/services/hotel/hotel.service';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { SingleRoomReservation } from 'src/app/model/hotel/single-room-reservation';
import { ReservationRequest } from 'src/app/model/hotel/reservation-request';

@Component({
  selector: 'app-book-form',
  templateUrl: './book-form.component.html',
  styleUrls: ['./book-form.component.css', '../../../shared/css/inputField.css']
})
export class BookFormComponent implements OnInit {
  @Input() rooms: Room[] = [];
  @Input() additionalServices: AdditionalService[] = [];
  visibleRooms: Room[] = []; // sobe koje ce se prikazati posle search-a

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
    private tokenService: TokenStorageService) {

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
    console.log('izabrani tip ' + this.bookForm.value.type);

    for (const room of this.rooms) {
      console.log('naisli na tip ' + room.type);
      if (room.type === this.bookForm.value.type) {
        console.log('room number ' + room.roomNumber + ' tip ' + room.type);
        if (room.price >= this.bookForm.value.priceRange[0] && room.price <= this.bookForm.value.priceRange[1]) {
          console.log('okej');
          if (this.isAvailable(room)) {
            this.visibleRooms.push(room);
            console.log(this.visibleRooms);
          }
        }
      }
    }
  }

  isAvailable(room: Room) {
    console.log('isAvabile in Room');
    const date0 = new Date(this.bookForm.value.bsRangeValue[0]);
    const datum0 = formatDate(date0, 'yyyy-MM-dd', 'en');
    const date1 = new Date(this.bookForm.value.bsRangeValue[1]);
    const datum1 = formatDate(date1, 'yyyy-MM-dd', 'en');

    for (const r of room.reservations) {
      console.log('aaaa,bbbb');
      console.log(r);

      if (datum0 >= r.roomReservation.checkInDate.toString() &&
        datum0 <= r.roomReservation.checkOutDate.toString()) {
        return false;
      }
      if (datum1 >= r.roomReservation.checkInDate.toString() &&
        datum1 <= r.roomReservation.checkOutDate.toString()) {
        return false;
      }
    }
    return true;
  }

  clickCheck(data) {
    this.checkedAdditionalServices.push(data);
    console.log('dodavanje', this.checkedAdditionalServices);
  }

  clickX(data) {
    // this.checkedAdditionalServices
    const index: number = this.checkedAdditionalServices.indexOf(data);
    if (index !== -1) {
      this.checkedAdditionalServices.splice(index, 1);
    }
    console.log('uklanjanje', this.checkedAdditionalServices);
  }

  clickBook(data) {
    this.bookedRooms.push(data);
    console.log('dodavanje', this.bookedRooms);

  }

  clickUnbook(data) {
    const index: number = this.bookedRooms.indexOf(data);
    if (index !== -1) {
      this.bookedRooms.splice(index, 1);
    }
    console.log('uklanjanje', this.bookedRooms);
  }

  completeBooking() {
    console.log('completeBooking');
    const roomReservation = new ReservationRequest();
    roomReservation.checkInDate = this.bookForm.value.bsRangeValue[0];
    roomReservation.checkOutDate = this.bookForm.value.bsRangeValue[1];
    roomReservation.additionalServices = this.checkedAdditionalServices;
    roomReservation.reservations = this.bookedRooms;
    console.log('salje se ', roomReservation);

    this.hotelService.rentRoom(roomReservation, this.tokenService.getUsername()).subscribe(
      roomReservationData => {
        console.log('vraceno', roomReservationData);
      },
      error => {
        console.log(error);
      }
    );
  }
}
