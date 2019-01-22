import { Location } from './../location';
import { Destination } from './destination';
export class AirCompany {
    id: number;
    name: string;
    description: string;
    destinations: Destination[];
    location: Location = new Location();
}
