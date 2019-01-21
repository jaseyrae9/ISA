import { Destination } from './destination';
export class AirCompany {
    id: number;
    name: string;
    description: string;
    destinations: Destination[];

    constructor(id?: number, name?: string, description?: string) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.destinations = [];
    }
}
