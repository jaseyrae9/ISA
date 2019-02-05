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
}
