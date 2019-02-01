import { Room } from 'src/app/model/hotel/room';
import { AdditionalService } from '../additional-service';
import { Location } from './../location';

export class Hotel {
    id: number;
    name: string;
    description: string;
    rooms: Room[];
    additionalServices: AdditionalService[] = [];
    location: Location = new Location();
    rating: number;
}
