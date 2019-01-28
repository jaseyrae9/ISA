import { AirCompany } from 'src/app/model/air-company/air-company';
import { Destination } from './destination';
import { Airplane } from 'src/app/model/air-company/airplane';
export class Flight {
    id: number;
    airplane: Airplane = new Airplane();
    destinations: FlightDestination[] = [];
    startDateAndTime: Date;
    endDateAndTime: Date;
    duration: string;
    length = 0;
    airCompanyBasicInfo: AirCompany = new AirCompany();
    maxCarryOnBags = 0;
    maxCheckedBags = 0;
    additionalServicesAvailable = false;
    status = 'IN_PROGRESS';
}

export class FlightDestination {
   id: number;
   destination: Destination = new Destination();
}
