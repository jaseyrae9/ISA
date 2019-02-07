import { FlightReservation } from './../air-company/flight-reservation';
import { CarReservation } from '../rent-a-car-company/car-reservation';
import { RoomReservation } from '../hotel/room-reservation';

export class Reservation {
    id: number;
    carReservation: CarReservation;
    roomReservation: RoomReservation;
    flightReservation: FlightReservation;
    creationDate: Date = new Date();
    discount: 0;
}
