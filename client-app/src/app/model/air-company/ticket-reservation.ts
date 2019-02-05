import { Ticket } from './ticket';
export class TicketReservation {
  id: number;
  firstName: string;
  lastName: string;
  passport: string;
  ticket: Ticket = new Ticket();
  status = -1;
  friend_id = -1;
  inviteStatus: string = null;
}
