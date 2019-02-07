import { SingleRoomReservation } from './single-room-reservation';
import { Hotel } from './hotel';

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
    averageRating: number;
    hotel: Hotel;

}
