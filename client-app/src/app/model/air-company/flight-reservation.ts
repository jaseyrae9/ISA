import { TicketReservation } from './ticket-reservation';
import { Flight } from './flight';
export class FlightReservation {
  id: number;
  flight: Flight = new Flight();
  ticketReservations: TicketReservation[] = [];
  total = 0;
}
