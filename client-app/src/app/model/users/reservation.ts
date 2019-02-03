import { CarReservation } from '../rent-a-car-company/car-reservation';
import { RoomReservation } from '../hotel/room-reservation';

export class Reservation {
    id: number;
    carReservation: CarReservation;
    roomReservation: RoomReservation;
    creationDate: Date = new Date();
}
