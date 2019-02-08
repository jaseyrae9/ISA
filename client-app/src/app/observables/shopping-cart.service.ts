import { AlertService } from 'ngx-alerts';
import { CarReservation } from 'src/app/model/rent-a-car-company/car-reservation';
import { RoomReservation } from 'src/app/model/hotel/room-reservation';
import { FlightReservation } from './../model/air-company/flight-reservation';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

@Injectable({
  providedIn: 'root'
})
export class ShoppingCartService {
  private flightReservation = new BehaviorSubject<FlightReservation>(null);
  currentFlightReservation = this.flightReservation.asObservable();

  private roomReservation = new BehaviorSubject<RoomReservation>(null);
  currentRoomReservation = this.roomReservation.asObservable();

  private carReservation =  new BehaviorSubject<CarReservation>(null);
  currentCarReservation = this.carReservation.asObservable();

  constructor() { }

  changeFlightReservation(flightReservation: FlightReservation) {
    this.flightReservation.next(JSON.parse(JSON.stringify(flightReservation)));
  }

  changeRoomReservation(roomReservation: RoomReservation) {
    this.roomReservation.next(JSON.parse(JSON.stringify(roomReservation)));
  }

  changeCarReservation(carReservation: CarReservation) {
    this.carReservation.next(carReservation);
  }

}
