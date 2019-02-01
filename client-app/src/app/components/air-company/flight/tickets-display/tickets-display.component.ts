import { Ticket } from './../../../../model/air-company/ticket';
import { Flight } from './../../../../model/air-company/flight';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-tickets-display',
  templateUrl: './tickets-display.component.html',
  styleUrls: ['./tickets-display.component.css', '../../../../shared/css/seats.css', '../../../../shared/css/item.css']
})
export class TicketsDisplayComponent implements OnInit {
  @Input() flight: Flight = new Flight();
  @Input() useCheckboxes = false;
  @Input() seatDisabler = false;
  checkedTickets: Ticket[] = [];
  constructor() { }

  ngOnInit() {
    for (const row of this.flight.tickets) {
      for (const ticket of row) {
        if (ticket.status === 'UNAVIABLE') {
          this.checkedTickets.push(ticket);
        }
      }
    }
  }

  onTicketChecked(event: any, ticket: Ticket) {
    if (event.target.checked) {
      this.checkedTickets.push(ticket);
    } else {
      const index = this.checkedTickets.indexOf(ticket);
      this.checkedTickets.splice(index, 1);
    }
  }

}
