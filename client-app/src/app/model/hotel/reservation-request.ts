import { AdditionalService } from '../additional-service';
import { Room } from './room';

export class ReservationRequest {
    id: number;
    checkInDate: Date;
    checkOutDate: Date;
    additionalServices: AdditionalService[];
    reservations: Room[];
}
