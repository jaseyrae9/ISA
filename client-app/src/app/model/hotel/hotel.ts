import { Room } from 'src/app/model/hotel/room';
import { AdditionalService } from '../additional-service';
import { Location } from './../location';

export class Hotel {
    id: number;
    name: string;
    description: string;
    rooms: Room[];
    additionalServices: AdditionalService[] = [];
    rating: number;
    location: Location = new Location();

    // constructor(id?: number, name?: string, description?: string, rooms?: Room[],
    //      additionalServices?: AdditionalService[], rating?: number) {
    //     this.id = id;
    //     this.name = name;
    //     this.description = description;
    //     this.rooms = rooms;
    //     this.additionalServices = additionalServices;
    //     this.rating = rating;
    // }
}
