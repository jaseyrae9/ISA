import { SingleRoomReservation } from './single-room-reservation';

export class Room {
    id: number;
    floor: number;
    roomNumber: number;
    numberOfBeds: number;
    price: number;
    type: string;
    active: boolean;
    rating: number;
    singleRoomReservations: SingleRoomReservation[];
    reservations: SingleRoomReservation[];
    ratingCount: number;
    totalRating: number;
    isFast: boolean;
    startDate: Date;
    endDate: Date;
    discount: number;
}
