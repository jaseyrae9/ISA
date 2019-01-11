import { Room } from 'src/app/model/hotel/room';
import { AdditionalService } from '../additional-service';

export class Hotel {
    id: number;
    name: string;
    description: string;
    rooms: Room[];
    additionalServices: AdditionalService[];

    constructor(id?: number, name?: string, description?: string, rooms?: Room[], additionalServices?: AdditionalService[]) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rooms = rooms;
        this.additionalServices = additionalServices;
    }
}
