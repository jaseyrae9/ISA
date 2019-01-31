import { Flight } from './../../../../model/air-company/flight';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-tickets-display',
  templateUrl: './tickets-display.component.html',
  styleUrls: ['./tickets-display.component.css', '../../../../shared/css/seats.css', '../../../../shared/css/item.css']
})
export class TicketsDisplayComponent implements OnInit {
  @Input() flight: Flight = new Flight();
  constructor() { }

  ngOnInit() {
  }

}
