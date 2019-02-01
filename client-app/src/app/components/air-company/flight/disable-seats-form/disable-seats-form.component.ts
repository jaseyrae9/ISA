import { HttpErrorResponse } from '@angular/common/http';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { TicketsDisplayComponent } from './../tickets-display/tickets-display.component';
import { Subject } from 'rxjs/Subject';
import { FormGroup, FormBuilder } from '@angular/forms';
import { Flight } from './../../../../model/air-company/flight';
import { Component, OnInit, Input, ViewChild } from '@angular/core';

@Component({
  selector: 'app-disable-seats-form',
  templateUrl: './disable-seats-form.component.html',
  styleUrls: ['./disable-seats-form.component.css', '../../../../shared/css/inputField.css']
})
export class DisableSeatsFormComponent implements OnInit {
  @Input() flight: Flight;
  errorMessage: String = '';
  public onClose: Subject<Flight>;
  @ViewChild('tickets') tickets: TicketsDisplayComponent;

  constructor(public modalRef: BsModalRef, private aircompanyService: AirCompanyService) { }

  ngOnInit() {
    this.onClose = new Subject();
  }

  disableSeats() {
    const ticketsId = this.tickets.checkedTickets.map(function(a) {return a.id; });
    this.aircompanyService.disableSeats(this.flight.airCompanyBasicInfo.id, this.flight.id, ticketsId).subscribe(
      (data) => {
        this.onClose.next(data);
        this.modalRef.hide();
      },
      (err: HttpErrorResponse) => {
        // interceptor je hendlovao ove zahteve
        if (err.status === 401 || err.status === 403 || err.status === 404) {
          this.modalRef.hide();
          return;
        }
        this.errorMessage = err.error.details;
      }
    );
  }

}
