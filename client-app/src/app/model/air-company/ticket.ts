import { Seat } from './seat';
export class Ticket {
  id;
  price = 0;
  discount = 0;
  status = 'AVAILABLE';
  seat: Seat = new Seat();
}
