import { FlightReservation } from './../../../model/air-company/flight-reservation';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-tickets-reservation',
  templateUrl: './tickets-reservation.component.html',
  styleUrls: ['./tickets-reservation.component.css', './../../../shared/css/inputField.css']
})
export class TicketsReservationComponent implements OnInit {
  @Input() flightReservation: FlightReservation = new FlightReservation();
  constructor() { }

  ngOnInit() {
  }

  getPrice(): number {
    let total = 0;
    for (const reservation of this.flightReservation.ticketReservations) {
      total += reservation.ticket.price - reservation.ticket.discount;
    }
    return total;
  }
}
