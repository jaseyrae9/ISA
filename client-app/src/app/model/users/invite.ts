import { Reservation } from './reservation';
export class Invite {
  id: number;
  reservationDTO: Reservation;
  status = 'PENDING';
  invitedBy: string;
}
