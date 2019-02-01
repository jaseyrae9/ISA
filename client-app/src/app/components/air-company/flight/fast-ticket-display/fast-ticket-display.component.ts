import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { TicketForFastReservation } from './../../../../model/air-company/ticket-for-fast-reservation';
import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
@Component({
  selector: 'app-fast-ticket-display',
  templateUrl: './fast-ticket-display.component.html',
  styleUrls: ['./fast-ticket-display.component.css']
})
export class FastTicketDisplayComponent implements OnInit {
  @Input() fastTicket: TicketForFastReservation = new TicketForFastReservation();
  @Output() ticketDeleted: EventEmitter<Object> = new EventEmitter();
  constructor(public tokenService: TokenStorageService, private airService: AirCompanyService) { }

  ngOnInit() {
  }

  deleteTicket() {
    this.airService.deleteTicketForFastReservation(this.fastTicket.flight.airCompanyBasicInfo.id, this.fastTicket.id).subscribe(
      () => {
        this.ticketDeleted.emit(this.fastTicket);
      }
      // neuspesno brisanje ce handlovati interceptori
    );
  }

}
