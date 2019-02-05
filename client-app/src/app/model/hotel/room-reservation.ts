import { AdditionalService } from '../additional-service';
import { SingleRoomReservation } from './single-room-reservation';
import { Hotel } from './hotel';
import { Room } from './room';

export class RoomReservation {
    id: number;
    checkInDate: Date;
    checkOutDate: Date;
    additionalServices: AdditionalService[];
    roomReservations: Room[]; // Lazne
    reservations: Room[];
    hotel: Hotel = new Hotel();
    isHotelRated: Boolean;
    ratingCount: number;
    totalRating: number;
    isFastReservation: boolean;
}
