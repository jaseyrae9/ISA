import { SingleRoomReservation } from './single-room-reservation';

export class Room {
    id: number;
    floor: number;
    roomNumber: number;
    numberOfBeds: number;
    price: number;
    type: string;
    active: boolean;
    singleRoomReservations: SingleRoomReservation[];

}
