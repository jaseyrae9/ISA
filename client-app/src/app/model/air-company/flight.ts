import { Ticket } from './ticket';
import { AirCompany } from 'src/app/model/air-company/air-company';
import { Destination } from './destination';
import { Airplane } from 'src/app/model/air-company/airplane';
export class Flight {
    id: number;
    airplane: Airplane = new Airplane();
    destinations: FlightDestination[] = [];
    startDateAndTime: Date = new Date();
    endDateAndTime: Date = new Date();
    duration: string;
    length = 0;
    airCompanyBasicInfo: AirCompany = new AirCompany();
    maxCarryOnBags = 0;
    maxCheckedBags = 0;
    additionalServicesAvailable = false;
    status = 'IN_PROGRESS';
    economyPrice = 0;
    premiumEconomyPrice = 0;
    bussinessPrice = 0;
    firstPrice = 0;
    tickets: Ticket[][];
}

export class FlightDestination {
   id: number;
   destination: Destination = new Destination();
}
