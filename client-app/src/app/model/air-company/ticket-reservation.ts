import { Ticket } from './ticket';
export class TicketReservation {
  id: number;
  firstName: string;
  lastName: string;
  passport: string;
  ticket: Ticket = new Ticket();
}
