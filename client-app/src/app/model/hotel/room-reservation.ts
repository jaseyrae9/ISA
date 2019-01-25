import { AdditionalService } from '../additional-service';
import { SingleRoomReservation } from './single-room-reservation';

export class RoomReservation {
    id: number;
    checkInDate: Date;
    checkOutDate: Date;
    additionalServices: AdditionalService[];
    singleRoomReservations: SingleRoomReservation[];
}
