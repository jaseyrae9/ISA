import { HttpErrorResponse } from '@angular/common/http';
import { TicketsDisplayComponent } from './../tickets-display/tickets-display.component';
import { Ticket } from './../../../../model/air-company/ticket';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { Subject } from 'rxjs/Subject';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Flight } from './../../../../model/air-company/flight';
import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { tick } from '@angular/core/src/render3';

@Component({
  selector: 'app-create-fast-reservations-form',
  templateUrl: './create-fast-reservations-form.component.html',
  styleUrls: ['./create-fast-reservations-form.component.css', '../../../../shared/css/inputField.css']
})
export class CreateFastReservationsFormComponent implements OnInit {
  @Input() flight: Flight;
  errorMessage: String = '';
  form: FormGroup;
  public onClose: Subject<Flight>;
  @ViewChild('tickets') tickets: TicketsDisplayComponent;

  constructor(public modalRef: BsModalRef, private formBuilder: FormBuilder, private aircompanyService: AirCompanyService) { }

  ngOnInit() {
    this.form = this.formBuilder.group({
      discount: ['', [Validators.required, Validators.min(0)]]
    });
    this.onClose = new Subject();
  }

  submit() {
    const ticketsId = this.tickets.checkedTickets.map(function(a) {return a.id; });
    if (ticketsId.length === 0) {
      this.errorMessage = 'Please, select at least one ticket.';
      return;
    }
    const value = this.form.value;
    value.tickets = ticketsId;
    this.aircompanyService.createTicketsForFastReservation(this.flight.airCompanyBasicInfo.id, this.flight.id, value).subscribe(
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
