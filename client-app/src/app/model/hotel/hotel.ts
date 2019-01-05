import { Room } from 'src/app/model/hotel/room';

export class Hotel {
    id: number;
    name: string;
    description: string;
    rooms : Room[];

    constructor(id?: number, name?: string, description?: string, rooms?: Room[]) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rooms = rooms;
    }
}
