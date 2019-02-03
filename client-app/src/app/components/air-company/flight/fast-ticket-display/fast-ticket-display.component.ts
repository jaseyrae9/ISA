import { Router } from '@angular/router';
import { ReservationInformationComponent } from './../reservation-information/reservation-information.component';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { TicketForFastReservation } from './../../../../model/air-company/ticket-for-fast-reservation';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { NgxNotificationService } from 'ngx-notification';
@Component({
  selector: 'app-fast-ticket-display',
  templateUrl: './fast-ticket-display.component.html',
  styleUrls: ['./fast-ticket-display.component.css']
})
export class FastTicketDisplayComponent implements OnInit {
  @Input() fastTicket: TicketForFastReservation = new TicketForFastReservation();
  @Output() ticketDeleted: EventEmitter<Object> = new EventEmitter();
  modalRef: BsModalRef;

  constructor(private ngxNotificationService: NgxNotificationService, public tokenService: TokenStorageService,
    private airService: AirCompanyService, private modalService: BsModalService, private router: Router) { }

  ngOnInit() {
  }

  deleteTicket() {
    this.airService.deleteTicketForFastReservation(this.fastTicket.flight.airCompanyBasicInfo.id, this.fastTicket.id).subscribe(
      () => {
        this.ticketDeleted.emit(this.fastTicket);
        this.ngxNotificationService.sendMessage('Fast reservation deleted.', 'dark', 'bottom-right');
      }
      // neuspesno brisanje ce handlovati interceptori
    );
  }

  fastReservation() {
    const initialState = {
      isFastReservation: true
    };
    this.modalRef = this.modalService.show(ReservationInformationComponent, { initialState });
    this.modalRef.content.onClose.subscribe(value => {
      this.airService.fastReservation(this.fastTicket.id, value.passport).subscribe(
        () => {
          this.ngxNotificationService.sendMessage('Ticket reserved.', 'dark', 'bottom-right');
          this.router.navigate(['/history']);
        },
        (error) => {
          this.ngxNotificationService.sendMessage('Fast reservation failed.', 'dark', 'bottom-right');
          this.ticketDeleted.emit(this.fastTicket);
        }
      );
    });
  }

}
