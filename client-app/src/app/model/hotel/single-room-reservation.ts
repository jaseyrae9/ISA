import { RoomReservation } from './room-reservation';
import { Room } from './room';

export class SingleRoomReservation {
    id: number;
    roomReservation: RoomReservation;
    room: Room;
    startDateAndTime: Date;
    endDateAndTime: Date;
    duration: string;
    length = 0;
    isRoomRated: Boolean;
    active: Boolean;
}
