import { Location } from './../location';
import { Destination } from './destination';
import { AdditionalService } from '../additional-service';

export class AirCompany {
    id: number;
    name: string;
    description: string;
    destinations: Destination[];
    location: Location = new Location();
    additionalServices: AdditionalService[] = [];
}
