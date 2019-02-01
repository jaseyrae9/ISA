import { Flight } from './flight';
import { Ticket } from './ticket';

export class TicketForFastReservation {
  id: number;
  ticket: Ticket;
  flight: Flight;
}
