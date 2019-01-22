import { Seat } from './seat';

export class Airplane {
  id: number;
  name: string;
  status: string;
  colNum: number;
  rowNum: number;
  seatsPerCol: number;
  seats: Seat[];
}
