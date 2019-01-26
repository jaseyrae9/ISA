import { Destination } from './destination';
import { Airplane } from 'src/app/model/air-company/airplane';
export class Flight {
    id: number;
    airplane: Airplane = new Airplane();
    destinations: Destination[] = [];
    startDateAndTime: Date;
    endDateAndTime: Date;
    duration: string;
    length = 0;
}
