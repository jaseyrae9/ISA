import { Component, OnInit, Input } from '@angular/core';
import { RoomReservation } from 'src/app/model/hotel/room-reservation';

@Component({
  selector: 'app-hotel-reservation',
  templateUrl: './hotel-reservation.component.html',
  styleUrls: ['./hotel-reservation.component.css', './../../../shared/css/inputField.css']
})
export class HotelReservationComponent implements OnInit {
  @Input() reservation: RoomReservation;

  constructor() { }

  ngOnInit() {
  }

}
